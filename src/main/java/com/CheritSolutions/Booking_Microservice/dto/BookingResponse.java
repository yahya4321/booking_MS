package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
     private UUID id;
    private UUID clientId;
    private UUID serviceId;
    private String serviceName;
    private BigDecimal servicePrice;
    private UUID staffId;
    private String staffName;
    private String staffPosition;
    private UUID businessId;
    private String businessName;
    private String businessAddress;
    private Instant slotStart;
    private Integer duration;
    private UUID slotId;
    private BookingStatus status;

    
}
