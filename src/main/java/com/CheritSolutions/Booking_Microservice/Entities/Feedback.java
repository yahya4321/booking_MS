package com.CheritSolutions.Booking_Microservice.Entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer serviceRating;

    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer staffRating;

    @Column(length = 500)
    @Size(max = 500)
    private String serviceComment;

    @Column(length = 500)
    @Size(max = 500)
    private String staffComment;

    @Column(nullable = false)
    private Boolean isAnonymous;

    @Column(nullable = false)
    private Instant createdAt;

    @Version
    private Integer version;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // Builder for Feedback
    public static class FeedbackBuilder {

        private UUID id;
        private Booking booking;
        private UUID clientId;
        private Integer serviceRating;
        private Integer staffRating;
        private String serviceComment;
        private String staffComment;
        private Boolean isAnonymous;
        private Instant createdAt;
        private Integer version;

        // Method to set the Booking
        public FeedbackBuilder booking(Booking booking) {
            this.booking = booking;
            return this;
        }

        // Method to set the ClientId
        public FeedbackBuilder clientId(UUID clientId) {
            this.clientId = clientId;
            return this;
        }

        // Method to set the Service Rating
        public FeedbackBuilder serviceRating(Integer serviceRating) {
            this.serviceRating = serviceRating;
            return this;
        }

        // Method to set the Staff Rating
        public FeedbackBuilder staffRating(Integer staffRating) {
            this.staffRating = staffRating;
            return this;
        }

        // Method to set the Service Comment
        public FeedbackBuilder serviceComment(String serviceComment) {
            this.serviceComment = serviceComment;
            return this;
        }

        // Method to set the Staff Comment
        public FeedbackBuilder staffComment(String staffComment) {
            this.staffComment = staffComment;
            return this;
        }

        // Method to set if the feedback is anonymous
        public FeedbackBuilder isAnonymous(Boolean isAnonymous) {
            this.isAnonymous = isAnonymous;
            return this;
        }

        // Method to set Created At
        public FeedbackBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        // Method to build Feedback object
        public Feedback build() {
            Feedback feedback = new Feedback();
            feedback.id = this.id;
            feedback.booking = this.booking;
            feedback.clientId = this.clientId;
            feedback.serviceRating = this.serviceRating;
            feedback.staffRating = this.staffRating;
            feedback.serviceComment = this.serviceComment;
            feedback.staffComment = this.staffComment;
            feedback.isAnonymous = this.isAnonymous;
            feedback.createdAt = this.createdAt;
            feedback.version = this.version;
            return feedback;
        }
    }

    // Static method to start the builder chain
    public static FeedbackBuilder builder() {
        return new FeedbackBuilder();
    }
}
