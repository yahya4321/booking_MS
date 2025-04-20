package com.CheritSolutions.Booking_Microservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
    @NotNull(message = "Booking ID is required")
    private UUID bookingId;

    @Min(1) @Max(5) @NotNull(message = "Service rating is required")
    private Integer serviceRating;

    @Min(1) @Max(5) @NotNull(message = "Staff rating is required")
    private Integer staffRating;

    @Size(max = 500, message = "Service comment must be 500 characters or less")
    private String serviceComment;

    @Size(max = 500, message = "Staff comment must be 500 characters or less")
    private String staffComment;

    @NotNull(message = "Anonymous flag is required")
    private Boolean isAnonymous;

    public FeedbackRequest(@NotNull(message = "Booking ID is required") UUID bookingId) {
        this.bookingId = bookingId;
    }
}
