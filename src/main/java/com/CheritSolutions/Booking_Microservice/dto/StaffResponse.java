package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private Integer postBufferTime;
    @JsonIgnore
    private BusinessResponse business;
    private JsonNode schedule;
    private List<AvailableSlotDTO> availabilitySlots;
    private UUID serviceId;
    private BigDecimal averageRating;
    private Integer ratingCount;
    private String email;
    private String keycloakUserId;
    private String invitationStatus;

    // Getters and Setters
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public Integer getRatingCount() { return ratingCount; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getPostBufferTime() { return postBufferTime; }
    public void setPostBufferTime(Integer postBufferTime) { this.postBufferTime = postBufferTime; }
    public BusinessResponse getBusiness() { return business; }
    public void setBusiness(BusinessResponse business) { this.business = business; }
    public JsonNode getSchedule() { return schedule; }
    public void setSchedule(JsonNode schedule) { this.schedule = schedule; }
    public List<AvailableSlotDTO> getAvailabilitySlots() { return availabilitySlots; }
    public void setAvailabilitySlots(List<AvailableSlotDTO> availabilitySlots) { this.availabilitySlots = availabilitySlots; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }
    public String getInvitationStatus() { return invitationStatus; }
    public void setInvitationStatus(String invitationStatus) { this.invitationStatus = invitationStatus; }
}