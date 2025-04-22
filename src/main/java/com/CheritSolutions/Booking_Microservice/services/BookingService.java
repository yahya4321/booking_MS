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

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.oauth2.jwt.Jwt;


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
          } else {
          }


// Send confirmation email to business
String businessEmail = business.getEmail(); // Assuming BusinessResponse has getEmail()
if (businessEmail != null && !businessEmail.isEmpty()) {
    emailService.sendBusinessBookingConfirmationEmail(businessEmail, business.getName(), response);
} else {
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
    return modelMapper.map(booking, BookingResponse.class);
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
                .orElseThrow(() -> {
                    return new BookingException("Booking not found: " + bookingId);
                });
            return modelMapper.map(booking, BookingResponse.class);
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
        throw new BookingException("Failed to cancel slot", e);
    }
}}