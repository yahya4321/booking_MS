package com.CheritSolutions.Booking_Microservice.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.*;

public class FeedbackRequest {
    
    @JsonIgnore // prevents deserialization from JSON
    private UUID bookingId;
    @Min(1)
    @Max(5)
    @NotNull(message = "Service rating is required")
    private Integer serviceRating;

    @Min(1)
    @Max(5)
    @NotNull(message = "Staff rating is required")
    private Integer staffRating;

    @Size(max = 500, message = "Service comment must be 500 characters or less")
    private String serviceComment;

    @Size(max = 500, message = "Staff comment must be 500 characters or less")
    private String staffComment;

    @NotNull(message = "Anonymous flag is required")
    private Boolean isAnonymous;

    // Default constructor
    public FeedbackRequest() {}

    // Constructor with bookingId only
    public FeedbackRequest(UUID bookingId) {
        this.bookingId = bookingId;
    }

    // Full constructor
    public FeedbackRequest(UUID bookingId, Integer serviceRating, Integer staffRating,
                           String serviceComment, String staffComment, Boolean isAnonymous) {
        this.bookingId = bookingId;
        this.serviceRating = serviceRating;
        this.staffRating = staffRating;
        this.serviceComment = serviceComment;
        this.staffComment = staffComment;
        this.isAnonymous = isAnonymous;
    }

    // Getters and setters
    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
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
}
