package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private UUID serviceId;
    private UUID businessId;
    private UUID staffId;
    private Instant slotStart;
   // private Integer duration;
}