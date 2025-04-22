package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;
import java.util.UUID;

public class BookingRequest {
    private UUID serviceId;
    private UUID businessId;
    private UUID staffId;
    private Instant slotStart;

    // No-args constructor
    public BookingRequest() {}

    // All-args constructor
    public BookingRequest(UUID serviceId, UUID businessId, UUID staffId, Instant slotStart) {
        this.serviceId = serviceId;
        this.businessId = businessId;
        this.staffId = staffId;
        this.slotStart = slotStart;
    }

    // Getters and Setters

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public Instant getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(Instant slotStart) {
        this.slotStart = slotStart;
    }
}
