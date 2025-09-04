package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AvailableSlotDTO {
    private Instant startTime;
    private Instant endTime;
    private boolean available;
    private String status; // "AVAILABLE", "BUFFER", "BOOKED"

    // No-args constructor (needed for serialization/deserialization)
    public AvailableSlotDTO() {}

    // Constructor for available slots
    public AvailableSlotDTO(Instant startTime, Instant endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
        this.status = "AVAILABLE";
    }

    // Getters and Setters

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDuration() {
        if (startTime == null || endTime == null) {
            return 0L;
        }
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }
}
