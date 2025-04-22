package com.CheritSolutions.Booking_Microservice.dto;

import java.time.Instant;
import java.util.UUID;

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

    // Default constructor
    public FeedbackResponse() {
    }

    // All-args constructor
    public FeedbackResponse(UUID id, UUID bookingId, UUID clientId,
                            Integer serviceRating, Integer staffRating,
                            String serviceComment, String staffComment,
                            Boolean isAnonymous, Instant createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.clientId = clientId;
        this.serviceRating = serviceRating;
        this.staffRating = staffRating;
        this.serviceComment = serviceComment;
        this.staffComment = staffComment;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public Integer getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }

    public Integer getStaffRating() {
        return staffRating;
    }

    public void setStaffRating(Integer staffRating) {
        this.staffRating = staffRating;
    }

    public String getServiceComment() {
        return serviceComment;
    }

    public void setServiceComment(String serviceComment) {
        this.serviceComment = serviceComment;
    }

    public String getStaffComment() {
        return staffComment;
    }

    public void setStaffComment(String staffComment) {
        this.staffComment = staffComment;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Optional: toString for debugging/logging
    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", clientId=" + clientId +
                ", serviceRating=" + serviceRating +
                ", staffRating=" + staffRating +
                ", serviceComment='" + serviceComment + '\'' +
                ", staffComment='" + staffComment + '\'' +
                ", isAnonymous=" + isAnonymous +
                ", createdAt=" + createdAt +
                '}';
    }
}
