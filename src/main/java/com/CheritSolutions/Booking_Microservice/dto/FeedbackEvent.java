package com.CheritSolutions.Booking_Microservice.dto;

import java.util.UUID;

public class FeedbackEvent {
    private UUID feedbackId;
    private UUID bookingId;
    private UUID serviceId;
    private UUID staffId;
    private Integer serviceRating;
    private Integer staffRating;

    // No-argument constructor
    public FeedbackEvent() {
    }

    // All-argument constructor
    public FeedbackEvent(UUID feedbackId, UUID bookingId, UUID serviceId, UUID staffId, Integer serviceRating, Integer staffRating) {
        this.feedbackId = feedbackId;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.staffId = staffId;
        this.serviceRating = serviceRating;
        this.staffRating = staffRating;
    }

    // Getters and Setters
    public UUID getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(UUID feedbackId) {
        this.feedbackId = feedbackId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
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
}
