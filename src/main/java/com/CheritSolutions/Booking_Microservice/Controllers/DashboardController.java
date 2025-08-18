package com.CheritSolutions.Booking_Microservice.Controllers;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.CheritSolutions.Booking_Microservice.dto.DashboardResponse;
import com.CheritSolutions.Booking_Microservice.dto.TrendResponse;
import com.CheritSolutions.Booking_Microservice.services.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

   @GetMapping("/{businessId}")
public ResponseEntity<DashboardResponse> getDashboardMetrics(
        @PathVariable UUID businessId,
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate) {
    try {
        String ownerId = jwt.getSubject(); // Keep as String
        log.debug("Fetching dashboard metrics for businessId: {} by ownerId: {}", businessId, ownerId);
        DashboardResponse response = dashboardService.getDashboardMetrics(businessId, ownerId, startDate, endDate);
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        log.error("Unauthorized access or invalid business ID: {}", e.getMessage());
        return ResponseEntity.status(403).body(null);
    } catch (Exception e) {
        log.error("Failed to fetch dashboard metrics: {}", e.getMessage());
        return ResponseEntity.status(500).body(null);
    }
}

    @GetMapping("/{businessId}/trends")
    public ResponseEntity<TrendResponse> getBookingTrends(
            @PathVariable UUID businessId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate) {
        try {
            String ownerId = jwt.getSubject(); // Keep as String
            log.debug("Fetching booking trends for businessId: {} by ownerId: {}", businessId, ownerId);
            TrendResponse response = dashboardService.getBookingTrends(businessId, ownerId, startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Unauthorized access or invalid business ID: {}", e.getMessage());
            return ResponseEntity.status(403).body(null);
        } catch (Exception e) {
            log.error("Failed to fetch booking trends: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}