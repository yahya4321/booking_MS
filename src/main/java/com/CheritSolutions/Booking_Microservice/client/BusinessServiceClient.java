package com.CheritSolutions.Booking_Microservice.client;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.CheritSolutions.Booking_Microservice.config.FeignConfig;
import com.CheritSolutions.Booking_Microservice.config.FeignErrorDecoder;
import com.CheritSolutions.Booking_Microservice.dto.AvailableSlotDTO;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackRequest;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;

@FeignClient(name = "BUSINESS", configuration = {FeignErrorDecoder.class, FeignConfig.class})
public interface BusinessServiceClient {

    @Cacheable("businesses")
    @GetMapping("/api/v1/businesses/{id}")
    BusinessResponse getBusinessById(@PathVariable UUID id);

    @Cacheable("businessSearch")
    @GetMapping("/api/v1/businesses/search")
    List<BusinessResponse> searchBusiness(@RequestParam("q") String query);

    @Cacheable("serviceSearch")
    @GetMapping("/api/v1/businesses/{businessId}/services/search")
    ResponseEntity<List<ServiceResponse>> searchServicesInBusiness(
            @PathVariable UUID businessId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice);

    @Cacheable("allServicesSearch")
    @GetMapping("/api/v1/businesses/services/search")
    ResponseEntity<List<ServiceResponse>> searchAllServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice);

    @Cacheable("services")
    @GetMapping("/api/v1/businesses/{businessId}/services")
    List<ServiceResponse> getServicesByBusiness(@PathVariable UUID businessId);

    @Cacheable("allBusinesses")
    @GetMapping("/api/v1/businesses")
    List<BusinessResponse> getAllbusinesses();

    @Cacheable("specificService")
    @GetMapping("/api/v1/businesses/{businessId}/services/{serviceId}")
    ServiceResponse getServiceByBusiness(@PathVariable UUID businessId,
                                        @PathVariable UUID serviceId);

    @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}/availability")
    boolean validateStaffAvailability(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestParam Instant slotStart,
            @RequestParam Integer duration);

    @Cacheable("staff")
    @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}")
    ResponseEntity<StaffResponse> getStaff(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId);

    @CacheEvict(value = {"staffSlots", "availableSlots"}, key = "#businessId + ':' + #staffId")
    @PostMapping("/api/v1/businesses/{businessId}/staff/{staffId}/reserve")
    UUID reserveSlots(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestParam Instant start,
            @RequestParam int duration);

    @Cacheable("availableSlots")
    @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}/available-slots")
    List<AvailableSlotDTO> getAvailableSlots(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestParam int duration);

    @CacheEvict(value = {"staffSlots", "availableSlots"}, key = "#businessId + ':' + #staffId")
    @DeleteMapping("/api/v1/businesses/{businessId}/staff/{staffId}/reservations/{slotId}")
    Void cancelReservation(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @PathVariable UUID slotId);

    @CacheEvict(value = {"services", "serviceRatings"}, key = "#businessId")
    @PostMapping("/api/v1/businesses/{businessId}/services/{serviceId}/feedback")
    void submitServiceFeedback(
            @PathVariable UUID businessId,
            @PathVariable UUID serviceId,
            @RequestBody FeedbackRequest feedbackRequest);

    @CacheEvict(value = {"staff", "staffRatings"}, key = "#businessId + ':' + #staffId")
    @PostMapping("/api/v1/businesses/{businessId}/staff/{staffId}/feedback")
    void submitStaffFeedback(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestBody FeedbackRequest feedbackRequest);

    @Cacheable("serviceRatings")
    @GetMapping("/api/v1/businesses/{businessId}/services/average-rating")
    Double getAverageServiceRating(@PathVariable UUID businessId);

    @Cacheable("staffRatings")
    @GetMapping("/api/v1/businesses/{businessId}/staff/average-rating")
    Double getAverageStaffRating(@PathVariable UUID businessId);

    @Cacheable("allStaff")
    @GetMapping("/api/v1/businesses/{businessId}/staff")
    ResponseEntity<List<StaffResponse>> getAllStaffByBusiness(@PathVariable("businessId") UUID businessId);

    @Cacheable("staffSlots")
    @GetMapping("/api/v1/businesses/{businessId}/staff/{staffId}/all-slots")
    List<AvailableSlotDTO> getAllStaffSlots(
            @PathVariable UUID businessId,
            @PathVariable UUID staffId,
            @RequestParam Instant startDate,
            @RequestParam Instant endDate);


@GetMapping("/api/v1/businesses/staff/keycloak")
    StaffResponse getStaffByKeycloakUserId(@RequestParam("keycloakUserId") String keycloakUserId);


@GetMapping("/api/businesses/{businessId}/owner")
        String getBusinessOwner(@PathVariable UUID businessId);


}