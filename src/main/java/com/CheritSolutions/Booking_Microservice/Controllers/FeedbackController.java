package com.CheritSolutions.Booking_Microservice.Controllers;

import com.CheritSolutions.Booking_Microservice.dto.FeedbackRequest;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackResponse;
import com.CheritSolutions.Booking_Microservice.services.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookings")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/{bookingId}/feedback")
    public ResponseEntity<FeedbackResponse> submitFeedback(
        @PathVariable UUID bookingId,
        @Valid @RequestBody FeedbackRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            if (!bookingId.equals(request.getBookingId())) {
                log.error("Booking ID mismatch: path={} vs body={}", bookingId, request.getBookingId());
                return ResponseEntity.badRequest().body(null);
            }
            UUID clientId = UUID.fromString(jwt.getSubject());
            FeedbackResponse response = feedbackService.submitFeedback(request, clientId);
            return ResponseEntity.status(201).body(response);
        } catch (BookingException e) {
            log.error("Feedback error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{bookingId}/feedback")
    public ResponseEntity<FeedbackResponse> getFeedback(
        @PathVariable UUID bookingId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        try {
            UUID clientId = UUID.fromString(jwt.getSubject());
            FeedbackResponse response = feedbackService.getFeedbackByBookingId(bookingId, clientId);
            return ResponseEntity.ok(response);
        } catch (BookingException e) {
            log.error("Feedback error: {}", e.getMessage());
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}