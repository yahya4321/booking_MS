package com.CheritSolutions.Booking_Microservice.client;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CheritSolutions.Booking_Microservice.config.FeignConfig;
import com.CheritSolutions.Booking_Microservice.config.FeignErrorDecoder;
import com.CheritSolutions.Booking_Microservice.dto.AvailableSlotDTO;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.CheritSolutions.Booking_Microservice.dto.FeedbackRequest;
@FeignClient(name = "BUSINESS", configuration = {FeignErrorDecoder.class, FeignConfig.class}) 
public interface BusinessServiceClient {

    @GetMapping("/api/v1/businesses/{id}")
    BusinessResponse getBusinessById(@PathVariable UUID id);
    

    @GetMapping("/api/v1/businesses/search")
    List<BusinessResponse> searchBusiness(@RequestParam("q") String query);
    

    @GetMapping("/api/v1/businesses/{businessId}/services/search")
    ResponseEntity<List<ServiceResponse>> searchServicesInBusiness(@PathVariable UUID businessId,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice);



    @GetMapping("/api/v1/businesses/services/search")
     ResponseEntity<List<ServiceResponse>> searchAllServices(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) BigDecimal minPrice,
    @RequestParam(required = false) BigDecimal maxPrice);



    @GetMapping("/api/v1/businesses/{businessId}/services")
    List<ServiceResponse> getServicesByBusiness(@PathVariable UUID businessId);


    @GetMapping("/api/v1/businesses")
    List<BusinessResponse> getAllbusinesses();

    @GetMapping("/api/v1/businesses/{businessId}/services/{serviceId}")
         ServiceResponse getServiceByBusiness(@PathVariable UUID businessId, 
         @PathVariable UUID serviceId);


         @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}/availability")
         boolean validateStaffAvailability(
             @PathVariable UUID businessId,
             @PathVariable UUID staffId,
             @RequestParam Instant slotStart,
             @RequestParam Integer duration // Add this
         );

         @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}")
         ResponseEntity<StaffResponse> getStaff(
             @PathVariable UUID businessId,
             @PathVariable UUID staffId);





     @PostMapping("/api/v1/businesses/{businessId}/staff/{staffId}/reserve")
     UUID reserveSlots(
         @PathVariable UUID businessId,
         @PathVariable UUID staffId,
         @RequestParam Instant start,
         @RequestParam int duration);
     
     @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}/available-slots")
     List<AvailableSlotDTO> getAvailableSlots(
         @PathVariable UUID businessId,
         @PathVariable UUID staffId,
         @RequestParam int duration);
        



         @DeleteMapping("/api/v1/businesses/{businessId}/staff/{staffId}/reservations/{slotId}")
    Void cancelReservation(
        @PathVariable UUID businessId,
        @PathVariable UUID staffId,
        @PathVariable UUID slotId);






        @PostMapping("/api/v1/businesses/{businessId}/services/{serviceId}/feedback")
        void submitServiceFeedback(
            @PathVariable UUID businessId,
            @PathVariable UUID serviceId,
            @RequestBody FeedbackRequest feedbackRequest
        );
    
        @PostMapping("/api/v1/businesses/{businessId}/staff/{staffId}/feedback")
        void submitStaffFeedback(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestBody FeedbackRequest feedbackRequest
        );
        }
        