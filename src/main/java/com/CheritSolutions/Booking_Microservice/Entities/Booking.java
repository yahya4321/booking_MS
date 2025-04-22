package com.CheritSolutions.Booking_Microservice.Entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID businessId;

    @Column(nullable = false)
    private UUID serviceId;

    @Column(nullable = false)
    private UUID staffId;

    @Column(nullable = false)
    private Instant slotStart;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = true) // Nullable if not all bookings have a slot ID initially
    private UUID slotId; // New field to store AvailabilitySlot ID

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Version
    private Integer version;

    // Getters and Setters (you can remove these if you use Lombok @Getter and @Setter)

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public UUID getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public UUID getStaffId() {
        return this.staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public Instant getSlotStart() {
        return this.slotStart;
    }

    public void setSlotStart(Instant slotStart) {
        this.slotStart = slotStart;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public UUID getSlotId() {
        return this.slotId;
    }

    public void setSlotId(UUID slotId) {
        this.slotId = slotId;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // Builder for Booking
    public static class BookingBuilder {

        private UUID id;
        private UUID clientId;
        private UUID businessId;
        private UUID serviceId;
        private UUID staffId;
        private Instant slotStart;
        private Integer duration;
        private UUID slotId;
        private BookingStatus status;
        private Integer version;

        // Method to set clientId
        public BookingBuilder clientId(UUID clientId) {
            this.clientId = clientId;
            return this;
        }

        // Method to set businessId
        public BookingBuilder businessId(UUID businessId) {
            this.businessId = businessId;
            return this;
        }

        // Method to set serviceId
        public BookingBuilder serviceId(UUID serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        // Method to set staffId
        public BookingBuilder staffId(UUID staffId) {
            this.staffId = staffId;
            return this;
        }

        // Method to set slotStart
        public BookingBuilder slotStart(Instant slotStart) {
            this.slotStart = slotStart;
            return this;
        }

        // Method to set duration
        public BookingBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        // Method to set slotId (optional)
        public BookingBuilder slotId(UUID slotId) {
            this.slotId = slotId;
            return this;
        }

        // Method to set status
        public BookingBuilder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        // Method to set version (optional)
        public BookingBuilder version(Integer version) {
            this.version = version;
            return this;
        }

        // Build the Booking object
        public Booking build() {
            Booking booking = new Booking();
            booking.id = this.id;
            booking.clientId = this.clientId;
            booking.businessId = this.businessId;
            booking.serviceId = this.serviceId;
            booking.staffId = this.staffId;
            booking.slotStart = this.slotStart;
            booking.duration = this.duration;
            booking.slotId = this.slotId;
            booking.status = this.status;
            booking.version = this.version;
            return booking;
        }
    }

    // Static method to start the builder chain
    public static BookingBuilder builder() {
        return new BookingBuilder();
    }
}
