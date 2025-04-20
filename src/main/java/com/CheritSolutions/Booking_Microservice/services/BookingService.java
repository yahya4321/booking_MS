// src/main/java/com/CheritSolutions/Booking_Microservice/services/BookingService.java
package com.CheritSolutions.Booking_Microservice.services;

import com.CheritSolutions.Booking_Microservice.dto.BookingRequest;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;
import com.CheritSolutions.Booking_Microservice.Controllers.BookingException;
import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;
import com.CheritSolutions.Booking_Microservice.repositories.BookingRepository;

import feign.FeignException;

import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
@Service
public class BookingService {

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
          log.info("Creating booking for client: {}", clientId);
          
          // Fetch service
          log.debug("Fetching service: {}/{}", request.getBusinessId(), request.getServiceId());
          ServiceResponse service = businessServiceClient.getServiceByBusiness(
              request.getBusinessId(), 
              request.getServiceId()
          );
          log.debug("Service found: {}", service.getName());
  
          // Fetch staff
          log.debug("Fetching staff: {}", request.getStaffId());
          StaffResponse staff = businessServiceClient.getStaff(
    request.getBusinessId(),
    request.getStaffId()
).getBody();
          if (staff == null) {
              throw new BookingException("Staff not found");
          }
          log.debug("Staff buffer time: {}", staff.getPostBufferTime());
  
  // Fetch business
            log.debug("Fetching business: {}", request.getBusinessId());
            BusinessResponse business = businessServiceClient.getBusinessById(request.getBusinessId());
            log.debug("Business found: {}", business.getName());





          // Calculate total duration
          int totalDuration = service.getDuration() + staff.getPostBufferTime();
  
          // Check availability
          log.debug("Checking availability for: {}", request.getSlotStart());
          boolean isAvailable = businessServiceClient.validateStaffAvailability(
              request.getBusinessId(),
              request.getStaffId(),
              request.getSlotStart(),
              totalDuration
          );
          log.debug("Availability check result: {}", isAvailable);
  
          if (!isAvailable) {
              throw new BookingException("Requested slot is not available");
          }
  
          // Reserve slot and get slotId
          log.debug("Reserving slots for staff: {}", request.getStaffId());
          UUID slotId = businessServiceClient.reserveSlots(
              request.getBusinessId(),
              request.getStaffId(),
              request.getSlotStart(),
              totalDuration
          );
          if (slotId == null) {
              throw new BookingException("Failed to reserve slot");
          }
  
          // Create booking with slotId
          Booking booking = Booking.builder()
              .clientId(clientId)
              .businessId(request.getBusinessId())
              .serviceId(request.getServiceId())
              .staffId(request.getStaffId())
              .slotStart(request.getSlotStart())
              .duration(service.getDuration())
              .slotId(slotId) // Store slotId
              .status(BookingStatus.CONFIRMED)
              .build();
          log.debug("Saving booking: {}", booking.getId());
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
            savedBooking.getStatus()
        );

          // Send confirmation email
          String userEmail = jwt.getClaimAsString("email");
          String userName = jwt.getClaimAsString("name");

          if (userEmail != null && !userEmail.isEmpty()) {
              emailService.sendBookingConfirmationEmail(userEmail,userName, response);
              log.debug("Triggered confirmation email to {} for booking ID: {}", userEmail, response.getId());
          } else {
              log.warn("No email found in JWT for client ID: {}", clientId);
          }


// Send confirmation email to business
String businessEmail = business.getEmail(); // Assuming BusinessResponse has getEmail()
if (businessEmail != null && !businessEmail.isEmpty()) {
    emailService.sendBusinessBookingConfirmationEmail(businessEmail, business.getName(), response);
    log.debug("Triggered business confirmation email to {} for booking ID: {}", businessEmail, response.getId());
} else {
    log.warn("No email found for business ID: {}", business.getId());
}


          return response;

      } catch (FeignException e) {
          log.error("Feign error: {} - {}", e.status(), e.contentUTF8());
          if (e.status() == 404) {
              throw new BookingException("Service or staff not found", e);
          } else if (e.status() == 409) {
              throw new BookingException("Slot conflict detected", e);
          }
          throw new BookingException("Booking failed: " + e.getMessage(), e);
      } catch (Exception e) {
          log.error("Booking failed", e);
          throw new BookingException("Booking failed: " + e.getMessage(), e);
      }
  }



public BookingResponse getBooking(UUID bookingId) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new BookingException("Booking not found"));
    return modelMapper.map(booking, BookingResponse.class);
}

@Transactional(readOnly = true)
    public BookingResponse getBookingById(UUID bookingId, UUID clientId) {
        try {
            if (bookingId == null) {
                log.error("Booking ID is null");
                throw new BookingException("Booking ID cannot be null");
            }
            if (clientId == null) {
                log.error("Client ID is null");
                throw new BookingException("Client ID cannot be null");
            }
            log.debug("Querying booking ID: {} for clientId: {}", bookingId, clientId);
            Booking booking = bookingRepository.findByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> {
                    log.warn("Booking not found for ID: {} and clientId: {}", bookingId, clientId);
                    return new BookingException("Booking not found: " + bookingId);
                });
            log.trace("Mapping booking ID: {}", booking.getId());
            return modelMapper.map(booking, BookingResponse.class);
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching booking ID: {} for clientId: {}", bookingId, clientId, e);
            throw new BookingException("Failed to fetch booking: " + e.getMessage(), e);
        }
    }

@Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByClient(UUID clientId) {
        try {
            if (clientId == null) {
                log.error("Client ID is null");
                throw new BookingException("Client ID cannot be null");
            }
            log.debug("Fetching bookings for clientId: {}", clientId);
            List<Booking> bookings = bookingRepository.findByClientId(clientId);
            log.debug("Found {} bookings for clientId: {}", bookings.size(), clientId);
            return bookings.stream().map(booking -> {
                // Fetch additional details
                ServiceResponse service = businessServiceClient.getServiceByBusiness(
                    booking.getBusinessId(), booking.getServiceId()
                );
                StaffResponse staff = businessServiceClient.getStaff(
                    booking.getBusinessId(), booking.getStaffId()
                ).getBody();
                BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());

                // Create enriched BookingResponse
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
                    booking.getStatus()
                );
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching bookings for clientId: {}", clientId, e);
            throw new BookingException("Failed to fetch client bookings: " + e.getMessage(), e);
        }
    }

@Transactional
public void cancelBooking(UUID bookingId, UUID clientId) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new BookingException("Booking not found"));
    if (!booking.getClientId().equals(clientId)) {
        throw new BookingException("Unauthorized to cancel this booking");
    }
    try {
        businessServiceClient.cancelReservation(
            booking.getBusinessId(),
            booking.getStaffId(),
            booking.getSlotId() // Assumes slotId is stored; adjust if needed
        );
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    } catch (FeignException e) {
        log.error("Failed to cancel slot: {}", e.getMessage());
        throw new BookingException("Failed to cancel slot", e);
    }
}}