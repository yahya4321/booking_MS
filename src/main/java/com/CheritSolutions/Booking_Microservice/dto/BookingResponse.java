package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;

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

    // No-args constructor
    public BookingResponse() {}

    // All-args constructor
    public BookingResponse(UUID id, UUID clientId, UUID serviceId, String serviceName,
                           BigDecimal servicePrice, UUID staffId, String staffName, String staffPosition,
                           UUID businessId, String businessName, String businessAddress,
                           Instant slotStart, Integer duration, UUID slotId, BookingStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffPosition = staffPosition;
        this.businessId = businessId;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.slotStart = slotStart;
        this.duration = duration;
        this.slotId = slotId;
        this.status = status;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPosition() {
        return staffPosition;
    }

    public void setStaffPosition(String staffPosition) {
        this.staffPosition = staffPosition;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Instant getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(Instant slotStart) {
        this.slotStart = slotStart;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public UUID getSlotId() {
        return slotId;
    }

    public void setSlotId(UUID slotId) {
        this.slotId = slotId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
