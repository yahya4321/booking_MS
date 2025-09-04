// src/main/java/com/CheritSolutions/Booking_Microservice/services/BookingService.java
package com.CheritSolutions.Booking_Microservice.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CheritSolutions.Booking_Microservice.Controllers.BookingException;
import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;
import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;
import com.CheritSolutions.Booking_Microservice.dto.BookingRequest;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;
import com.CheritSolutions.Booking_Microservice.repositories.BookingRepository;

import feign.FeignException;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusinessServiceClient businessServiceClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Retryable(
        value = FeignException.Conflict.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public BookingResponse createBooking(BookingRequest request, UUID clientId, Jwt jwt) {
        try {
            // Fetch service
            ServiceResponse service = businessServiceClient.getServiceByBusiness(
                request.getBusinessId(),
                request.getServiceId()
            );

            // Fetch staff
            StaffResponse staff = businessServiceClient.getStaff(
                request.getBusinessId(),
                request.getStaffId()
            ).getBody();
            if (staff == null) {
                throw new BookingException("Staff not found");
            }

            // Fetch business
            BusinessResponse business = businessServiceClient.getBusinessById(request.getBusinessId());

            // Calculate total duration
            int totalDuration = service.getDuration() + staff.getPostBufferTime();

            // Check availability
            boolean isAvailable = businessServiceClient.validateStaffAvailability(
                request.getBusinessId(),
                request.getStaffId(),
                request.getSlotStart(),
                totalDuration
            );

            if (!isAvailable) {
                throw new BookingException("Requested slot is not available");
            }

            // Reserve slot and get slotId
            UUID slotId = businessServiceClient.reserveSlots(
                request.getBusinessId(),
                request.getStaffId(),
                request.getSlotStart(),
                totalDuration
            );
            if (slotId == null) {
                throw new BookingException("Failed to reserve slot");
            }

            // Extract user details from JWT
            String clientName = jwt.getClaimAsString("name");
            String clientEmail = jwt.getClaimAsString("email");
            String clientPhone = jwt.getClaimAsString("phone_number");

            // Create booking with slotId and user details
            Booking booking = Booking.builder()
                .clientId(clientId)
                .businessId(request.getBusinessId())
                .serviceId(request.getServiceId())
                .staffId(request.getStaffId())
                .slotStart(request.getSlotStart())
                .duration(service.getDuration())
                .slotId(slotId)
                .status(BookingStatus.CONFIRMED)
                .clientName(clientName)
                .clientEmail(clientEmail)
                .clientPhone(clientPhone)
                .build();
            Booking savedBooking = bookingRepository.save(booking);

            // Create BookingResponse with enriched details
            BookingResponse response = new BookingResponse(
                savedBooking.getId(),
                savedBooking.getClientId(),
                savedBooking.getServiceId(),
                service.getName(),
                service.getBasePrice(),
                savedBooking.getStaffId(),
                staff.getName(),
                staff.getPosition(),
                savedBooking.getBusinessId(),
                business.getName(),
                business.getAddress(),
                savedBooking.getSlotStart(),
                savedBooking.getDuration(),
                savedBooking.getSlotId(),
                savedBooking.getStatus(),
                business.getLatitude(),
                business.getLongitude(),
                savedBooking.getClientName(),
                savedBooking.getClientEmail(),
                savedBooking.getClientPhone()
            );

            // Send confirmation email to client
            if (clientEmail != null && !clientEmail.isEmpty()) {
                emailService.sendBookingConfirmationEmail(clientEmail, clientName, response);
            } else {
                log.warn("No client email provided for booking ID: {}", savedBooking.getId());
            }

            // Send confirmation email to business
            String businessEmail = business.getEmail();
            if (businessEmail != null && !businessEmail.isEmpty()) {
                emailService.sendBusinessBookingConfirmationEmail(businessEmail, business.getName(), response);
            } else {
                log.warn("No business email provided for business ID: {}", business.getId());
            }

            return response;

        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BookingException("Service or staff not found", e);
            } else if (e.status() == 409) {
                throw new BookingException("Slot conflict detected", e);
            }
            throw new BookingException("Booking failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new BookingException("Booking failed: " + e.getMessage(), e);
        }
    }

    public BookingResponse getBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new BookingException("Booking not found"));

        // Fetch additional details
        ServiceResponse service = businessServiceClient.getServiceByBusiness(
            booking.getBusinessId(), booking.getServiceId()
        );
        StaffResponse staff = businessServiceClient.getStaff(
            booking.getBusinessId(), booking.getStaffId()
        ).getBody();
        BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());

        return new BookingResponse(
            booking.getId(),
            booking.getClientId(),
            booking.getServiceId(),
            service.getName(),
            service.getBasePrice(),
            booking.getStaffId(),
            staff != null ? staff.getName() : "Unknown",
            staff != null ? staff.getPosition() : "Unknown",
            booking.getBusinessId(),
            business.getName(),
            business.getAddress(),
            booking.getSlotStart(),
            booking.getDuration(),
            booking.getSlotId(),
            booking.getStatus(),
            business.getLatitude(),
            business.getLongitude(),
            booking.getClientName(),
            booking.getClientEmail(),
            booking.getClientPhone()
        );
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(UUID bookingId, UUID clientId) {
        try {
            if (bookingId == null) {
                throw new BookingException("Booking ID cannot be null");
            }
            if (clientId == null) {
                throw new BookingException("Client ID cannot be null");
            }
            Booking booking = bookingRepository.findByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> new BookingException("Booking not found: " + bookingId));

            // Fetch additional details
            ServiceResponse service = businessServiceClient.getServiceByBusiness(
                booking.getBusinessId(), booking.getServiceId()
            );
            StaffResponse staff = businessServiceClient.getStaff(
                booking.getBusinessId(), booking.getStaffId()
            ).getBody();
            BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());

            return new BookingResponse(
                booking.getId(),
                booking.getClientId(),
                booking.getServiceId(),
                service.getName(),
                service.getBasePrice(),
                booking.getStaffId(),
                staff != null ? staff.getName() : "Unknown",
                staff != null ? staff.getPosition() : "Unknown",
                booking.getBusinessId(),
                business.getName(),
                business.getAddress(),
                booking.getSlotStart(),
                booking.getDuration(),
                booking.getSlotId(),
                booking.getStatus(),
                business.getLatitude(),
                business.getLongitude(),
                booking.getClientName(),
                booking.getClientEmail(),
                booking.getClientPhone()
            );
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            throw new BookingException("Failed to fetch booking: " + e.getMessage(), e);
        }
    }

@Transactional(readOnly = true)
public List<BookingResponse> getBookingsByClient(UUID clientId) {
    try {
        if (clientId == null) {
            throw new BookingException("Client ID cannot be null");
        }
        List<Booking> bookings = bookingRepository.findByClientId(clientId);
        return bookings.stream().map(booking -> {
            try {
                // Fetch additional details
                ServiceResponse service = businessServiceClient.getServiceByBusiness(
                    booking.getBusinessId(), booking.getServiceId()
                );
                StaffResponse staff = businessServiceClient.getStaff(
                    booking.getBusinessId(), booking.getStaffId()
                ).getBody();
                BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());

                return new BookingResponse(
                    booking.getId(),
                    booking.getClientId(),
                    booking.getServiceId(),
                    service.getName(),
                    service.getBasePrice(),
                    booking.getStaffId(),
                    staff != null ? staff.getName() : "Unknown",
                    staff != null ? staff.getPosition() : "Unknown",
                    booking.getBusinessId(),
                    business.getName(),
                    business.getAddress(),
                    booking.getSlotStart(),
                    booking.getDuration(),
                    booking.getSlotId(),
                    booking.getStatus(),
                    business.getLatitude(),
                    business.getLongitude(),
                    booking.getClientName(),
                    booking.getClientEmail(),
                    booking.getClientPhone()
                );
            } catch (Exception e) {
                // Log and skip this booking if details can't be fetched
                log.warn("Failed to enrich booking {}: {}", booking.getId(), e.getMessage());
                return null;
            }
        })
        .filter(response -> response != null)
        .collect(Collectors.toList());
    } catch (Exception e) {
        throw new BookingException("Failed to fetch client bookings: " + e.getMessage(), e);
    }
}


@Transactional(readOnly = true)
public List<BookingResponse> getBookingsByStaff(Jwt jwt, int page, int size) {
    try {
        String keycloakUserId = jwt.getSubject();
        if (keycloakUserId == null || keycloakUserId.trim().isEmpty()) {
            throw new BookingException("JWT subject (keycloakUserId) is missing or empty");
        }
        log.debug("Fetching staff ID for keycloakUserId: {}", keycloakUserId);

        // Fetch staff details using keycloakUserId
        StaffResponse staff = businessServiceClient.getStaffByKeycloakUserId(keycloakUserId);
        if (staff == null) {
            throw new BookingException("Staff not found for keycloakUserId: " + keycloakUserId);
        }
        UUID staffId = staff.getId();
        log.debug("Found staffId: {} for keycloakUserId: {}", staffId, keycloakUserId);

        // Fetch bookings for the staff member
        Pageable pageable = PageRequest.of(page, size);
        List<Booking> bookings = bookingRepository.findByStaffId(staffId, pageable).getContent();

        // Map bookings to BookingResponse
        return bookings.stream().map(booking -> {
            try {
                ServiceResponse service = businessServiceClient.getServiceByBusiness(
                    booking.getBusinessId(), booking.getServiceId()
                );
                BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());

                return new BookingResponse(
                    booking.getId(),
                    booking.getClientId(),
                    booking.getServiceId(),
                    service.getName(),
                    service.getBasePrice(),
                    booking.getStaffId(),
                    staff.getName(),
                    staff.getPosition(),
                    booking.getBusinessId(),
                    business.getName(),
                    business.getAddress(),
                    booking.getSlotStart(),
                    booking.getDuration(),
                    booking.getSlotId(),
                    booking.getStatus(),
                    business.getLatitude(),
                    business.getLongitude(),
                    booking.getClientName(),
                    booking.getClientEmail(),
                    booking.getClientPhone()
                );
            } catch (FeignException.NotFound e) {
                log.warn("Service or business not found for bookingId: {}", booking.getId(), e);
                return null;
            } catch (FeignException e) {
                log.error("Failed to fetch details for bookingId: {}", booking.getId(), e);
                return null;
            }
        })
        .filter(response -> response != null)
        .collect(Collectors.toList());
    } catch (FeignException.NotFound e) {
        throw new BookingException("Staff not found");
    } catch (Exception e) {
        throw new BookingException("Failed to fetch staff bookings: " + e.getMessage(), e);
    }
}

    @Transactional
    public void cancelBooking(UUID bookingId, UUID clientId) {
        try {
            // Fetch and validate booking
            Booking booking = bookingRepository.findByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> new BookingException("Booking not found or not owned by client"));

            if (booking.getStatus() != BookingStatus.CONFIRMED) {
                throw new BookingException("Only confirmed bookings can be cancelled");
            }

            // Update booking status
            booking.setStatus(BookingStatus.CANCELLED);
            Booking savedBooking = bookingRepository.save(booking);

            // Fetch business, service, and staff details for enriched BookingResponse
            BusinessResponse business = businessServiceClient.getBusinessById(savedBooking.getBusinessId());
            ServiceResponse service = businessServiceClient.getServiceByBusiness(
                savedBooking.getBusinessId(), savedBooking.getServiceId());
            StaffResponse staff = savedBooking.getStaffId() != null
                ? businessServiceClient.getStaff(savedBooking.getBusinessId(), savedBooking.getStaffId()).getBody()
                : null;

            // Create enriched BookingResponse
            BookingResponse bookingResponse = new BookingResponse(
                savedBooking.getId(),
                savedBooking.getClientId(),
                savedBooking.getServiceId(),
                service.getName(),
                service.getBasePrice(),
                savedBooking.getStaffId(),
                staff != null ? staff.getName() : "Unknown",
                staff != null ? staff.getPosition() : "Unknown",
                savedBooking.getBusinessId(),
                business.getName(),
                business.getAddress(),
                savedBooking.getSlotStart(),
                savedBooking.getDuration(),
                savedBooking.getSlotId(),
                savedBooking.getStatus(),
                business.getLatitude(),
                business.getLongitude(),
                savedBooking.getClientName(),
                savedBooking.getClientEmail(),
                savedBooking.getClientPhone()
            );

            // Send cancellation emails
            if (savedBooking.getClientEmail() != null && !savedBooking.getClientEmail().isEmpty()) {
                emailService.sendBookingCancellationEmail(
                    savedBooking.getClientEmail(),
                    savedBooking.getClientName(),
                    bookingResponse
                );
            }
            if (business.getEmail() != null && !business.getEmail().isEmpty()) {
                emailService.sendBusinessBookingCancellationEmail(
                    business.getEmail(),
                    business.getName(),
                    bookingResponse
                );
            }
        } catch (FeignException e) {
            throw new BookingException("Failed to cancel booking due to external service error", e);
        }
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings(UUID businessId, String ownerId, BookingStatus status, int page, int size) {
        try {
            // Validate business ownership
            BusinessResponse business = businessServiceClient.getBusinessById(businessId);
            if (business == null || !business.getOwnerId().equals(ownerId)) {
                log.warn("Unauthorized access to business {} by ownerId {}", businessId, ownerId);
                throw new BookingException("Unauthorized access to business");
            }

            Pageable pageable = PageRequest.of(page, size);
            List<Booking> bookings;
            if (status != null) {
                bookings = bookingRepository.findByBusinessIdAndStatus(businessId, status, pageable).getContent();
            } else {
                bookings = bookingRepository.findByBusinessId(businessId, pageable).getContent();
            }
            return bookings.stream().map(booking -> {
                ServiceResponse service = null;
                StaffResponse staff = null;
                try {
                    service = businessServiceClient.getServiceByBusiness(
                        booking.getBusinessId(), booking.getServiceId()
                    );
                } catch (FeignException.NotFound e) {
                    log.warn("Service not found for businessId: {}, serviceId: {}", 
                        booking.getBusinessId(), booking.getServiceId(), e);
                } catch (FeignException e) {
                    log.error("Failed to fetch service for businessId: {}, serviceId: {}", 
                        booking.getBusinessId(), booking.getServiceId(), e);
                }
                try {
                    staff = businessServiceClient.getStaff(
                        booking.getBusinessId(), booking.getStaffId()
                    ).getBody();
                } catch (FeignException.NotFound e) {
                    log.warn("Staff not found for businessId: {}, staffId: {}", 
                        booking.getBusinessId(), booking.getStaffId(), e);
                } catch (FeignException e) {
                    log.error("Failed to fetch staff for businessId: {}, staffId: {}", 
                        booking.getBusinessId(), booking.getStaffId(), e);
                }
                return new BookingResponse(
                    booking.getId(),
                    booking.getClientId(),
                    booking.getServiceId(),
                    service != null ? service.getName() : "Unknown",
                    service != null ? service.getBasePrice() : BigDecimal.ZERO,
                    booking.getStaffId(),
                    staff != null ? staff.getName() : "Unknown",
                    staff != null ? staff.getPosition() : "Unknown",
                    booking.getBusinessId(),
                    business.getName(),
                    business.getAddress(),
                    booking.getSlotStart(),
                    booking.getDuration(),
                    booking.getSlotId(),
                    booking.getStatus(),
                    business.getLatitude(),
                    business.getLongitude(),
                    booking.getClientName(),
                    booking.getClientEmail(),
                    booking.getClientPhone()
                );
            }).collect(Collectors.toList());
        } catch (FeignException.NotFound e) {
            log.warn("Business not found for businessId: {}", businessId, e);
            throw new BookingException("Business not found");
        } catch (Exception e) {
            log.error("Failed to fetch bookings for businessId: {}: {}", businessId, e.getMessage(), e);
            throw new BookingException("Failed to fetch bookings: " + e.getMessage(), e);
        }
    }



    @Transactional
    public BookingResponse finishBooking(UUID bookingId, String keycloakUserId) {
        try {
            // Fetch booking
            Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking not found: " + bookingId));

            // Verify staff authorization
            StaffResponse staff = businessServiceClient.getStaffByKeycloakUserId(keycloakUserId);
            if (staff == null || !staff.getId().equals(booking.getStaffId())) {
                throw new BookingException("Unauthorized: User is not the assigned staff for this booking");
            }

            // Validate booking status
            if (booking.getStatus() != BookingStatus.CONFIRMED) {
                throw new BookingException("Only CONFIRMED bookings can be marked as FINISHED");
            }

            // Validate booking time
         /*   Instant now = Instant.now();
            if (booking.getSlotStart().isAfter(now)) {
                throw new BookingException("Cannot mark future bookings as FINISHED");
            }*/

            // Update booking status
            booking.setStatus(BookingStatus.FINISHED);
            Booking savedBooking = bookingRepository.save(booking);

            // Fetch additional details
            BusinessResponse business = businessServiceClient.getBusinessById(savedBooking.getBusinessId());
            ServiceResponse service = businessServiceClient.getServiceByBusiness(
                savedBooking.getBusinessId(), savedBooking.getServiceId());
            StaffResponse staffDetails = businessServiceClient.getStaff(
                savedBooking.getBusinessId(), savedBooking.getStaffId()).getBody();

            // Create enriched BookingResponse
            BookingResponse bookingResponse = new BookingResponse(
                savedBooking.getId(),
                savedBooking.getClientId(),
                savedBooking.getServiceId(),
                service.getName(),
                service.getBasePrice(),
                savedBooking.getStaffId(),
                staffDetails != null ? staffDetails.getName() : "Unknown",
                staffDetails != null ? staffDetails.getPosition() : "Unknown",
                savedBooking.getBusinessId(),
                business.getName(),
                business.getAddress(),
                savedBooking.getSlotStart(),
                savedBooking.getDuration(),
                savedBooking.getSlotId(),
                savedBooking.getStatus(),
                business.getLatitude(),
                business.getLongitude(),
                savedBooking.getClientName(),
                savedBooking.getClientEmail(),
                savedBooking.getClientPhone()
            );

            // Send emails
            if (savedBooking.getClientEmail() != null && !savedBooking.getClientEmail().isEmpty()) {
                emailService.sendBookingFinishedEmail(
                    savedBooking.getClientEmail(),
                    savedBooking.getClientName(),
                    bookingResponse
                );
            }
            if (business.getEmail() != null && !business.getEmail().isEmpty()) {
                emailService.sendBusinessBookingFinishedEmail(
                    business.getEmail(),
                    business.getName(),
                    bookingResponse
                );
            }

            log.info("Booking {} marked as FINISHED by staff with keycloakUserId: {}", bookingId, keycloakUserId);
            return bookingResponse;

        } catch (FeignException e) {
            throw new BookingException("Failed to mark booking as FINISHED due to external service error", e);
        } catch (Exception e) {
            throw new BookingException("Failed to mark booking as FINISHED: " + e.getMessage(), e);
        }
    }
}