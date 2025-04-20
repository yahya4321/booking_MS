package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private UUID id;
    private UUID bookingId;
    private UUID clientId;
    private Integer serviceRating;
    private Integer staffRating;
    private String serviceComment;
    private String staffComment;
    private Boolean isAnonymous;
    private Instant createdAt;
}