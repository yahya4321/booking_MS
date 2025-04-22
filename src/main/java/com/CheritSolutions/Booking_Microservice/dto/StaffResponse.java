package com.CheritSolutions.Booking_Microservice.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private Integer postBufferTime;
    private UUID businessId;
    private JsonNode schedule;
    private List<AvailableSlotDTO> availabilitySlots;
    private UUID serviceId;

    // Default constructor
    public StaffResponse() {
    }

    // All-args constructor
    public StaffResponse(UUID id, String name, String position, Integer postBufferTime, UUID businessId,
                         JsonNode schedule, List<AvailableSlotDTO> availabilitySlots, UUID serviceId) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.postBufferTime = postBufferTime;
        this.businessId = businessId;
        this.schedule = schedule;
        this.availabilitySlots = availabilitySlots;
        this.serviceId = serviceId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getPostBufferTime() {
        return postBufferTime;
    }

    public void setPostBufferTime(Integer postBufferTime) {
        this.postBufferTime = postBufferTime;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public JsonNode getSchedule() {
        return schedule;
    }

    public void setSchedule(JsonNode schedule) {
        this.schedule = schedule;
    }

    public List<AvailableSlotDTO> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailableSlotDTO> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    // toString method (optional for debugging/logging)
    @Override
    public String toString() {
        return "StaffResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", postBufferTime=" + postBufferTime +
                ", businessId=" + businessId +
                ", schedule=" + schedule +
                ", availabilitySlots=" + availabilitySlots +
                ", serviceId=" + serviceId +
                '}';
    }
}
