package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;

public class AvailableSlotDTO {
    private Instant startTime;
    private Instant endTime;
    private boolean available;
    private String status; // "AVAILABLE", "BUFFER", "BOOKED"


    public AvailableSlotDTO(Instant startTime, Instant endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
        this.status = "AVAILABLE";
    }
}

