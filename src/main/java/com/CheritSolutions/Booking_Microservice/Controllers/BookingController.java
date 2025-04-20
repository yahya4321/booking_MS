// src/main/java/com/CheritSolutions/Booking_Microservice/controllers/BookingController.java
package com.CheritSolutions.Booking_Microservice.Controllers;

import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;
import com.CheritSolutions.Booking_Microservice.dto.BookingRequest;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.services.BookingService;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BusinessServiceClient businessServiceClient;



    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
        @RequestBody BookingRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            UUID clientId = UUID.fromString(jwt.getSubject());
            BookingResponse response = bookingService.createBooking(request, clientId, jwt);
            return ResponseEntity.status(201).body(response);
        } catch (BookingException e) {
            log.error("Booking error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (FeignException.NotFound e) {
            log.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(404).body(null);
        } catch (FeignException.Conflict e) {
            log.error("Slot conflict: {}", e.getMessage());
            return ResponseEntity.status(409).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("Internal server error", e);
        }
    }


@GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
        @PathVariable UUID bookingId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            if (jwt == null) {
                log.error("JWT is null");
                return ResponseEntity.status(401).body(null);
            }
            String subject = jwt.getSubject();
            if (subject == null || subject.trim().isEmpty()) {
                log.error("JWT subject is missing or empty");
                return ResponseEntity.status(400).body(null);
            }
            UUID clientId = UUID.fromString(subject);
            log.debug("Fetching booking ID: {} for clientId: {}", bookingId, clientId);
            BookingResponse response = bookingService.getBookingById(bookingId, clientId);
            if (response == null) {
                log.warn("Booking not found for ID: {}", bookingId);
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in JWT subject: {}", jwt.getSubject(), e);
            return ResponseEntity.status(400).body(null);
        } catch (BookingException e) {
            log.error("Booking error for ID {}: {}", bookingId, e.getMessage());
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            log.error("Unexpected error fetching booking ID: {}", bookingId, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/client")
    public ResponseEntity<List<BookingResponse>> getClientBookings(
        @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            if (jwt == null) {
                log.error("JWT is null");
                return ResponseEntity.status(401).body(new ArrayList<>());
            }
            String subject = jwt.getSubject();
            if (subject == null || subject.trim().isEmpty()) {
                log.error("JWT subject is missing or empty");
                return ResponseEntity.status(400).body(new ArrayList<>());
            }
            UUID clientId = UUID.fromString(subject);
            log.debug("Fetching bookings for clientId: {}", clientId);
            List<BookingResponse> responses = bookingService.getBookingsByClient(clientId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in JWT subject: {}", jwt.getSubject(), e);
            return ResponseEntity.status(400).body(new ArrayList<>());
        } catch (BookingException e) {
            log.error("Booking error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new ArrayList<>());
        } catch (Exception e) {
            log.error("Unexpected error fetching client bookings", e);
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(
        @PathVariable UUID bookingId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID clientId = UUID.fromString(jwt.getSubject());
        bookingService.cancelBooking(bookingId, clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/business/{id}")
    public BusinessResponse getBusinessById(@PathVariable UUID id) {
        return businessServiceClient.getBusinessById(id);
    }


    @GetMapping("/business")
    public List<BusinessResponse> getAllbusinesses() {
        return businessServiceClient.getAllbusinesses();
    }

    @GetMapping("/business/search")
    public List<BusinessResponse> searchBusiness(@RequestParam("q") String query) {
        return businessServiceClient.searchBusiness(query);
    }


    @GetMapping("/business/{businessId}/services/search")
    public ResponseEntity<List<ServiceResponse>> searchServicesInBusiness(
    @PathVariable UUID businessId,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice) {
        return businessServiceClient.searchServicesInBusiness(businessId, name, minPrice, maxPrice);
    }

    @GetMapping("/businesses/services/search")
     ResponseEntity<List<ServiceResponse>> searchAllServices(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice){
        return businessServiceClient.searchAllServices(name, minPrice, maxPrice);
    }




  //  /api/v1/businesses/{businessId}/services/search

    @GetMapping("/business/{businessId}/services")
    public List<ServiceResponse>  getServicesByBusiness(@PathVariable UUID businessId) {
        return businessServiceClient.getServicesByBusiness(businessId);
    }

    @GetMapping("/business/{businessId}/services/{serviceId}")
    public ServiceResponse  getServiceByBusiness(@PathVariable UUID businessId, 
    @PathVariable UUID serviceId) {
        return businessServiceClient.getServiceByBusiness(businessId, serviceId);
    }
    @GetMapping("/{businessId}/staff/{staffId}/availability")
    public boolean validateStaffAvailability(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestParam Instant slotStart,
            @RequestParam Integer duration) {
        return businessServiceClient.validateStaffAvailability(businessId, staffId, slotStart, duration);
    }
        
    
}