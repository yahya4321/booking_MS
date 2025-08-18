// src/main/java/com/CheritSolutions/Booking_Microservice/Entities/Booking.java
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

    @Column(nullable = true)
    private UUID slotId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = true)
    private String clientPhone;

    @Version
    private Integer version;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }
    public UUID getBusinessId() { return businessId; }
    public void setBusinessId(UUID businessId) { this.businessId = businessId; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Instant getSlotStart() { return slotStart; }
    public void setSlotStart(Instant slotStart) { this.slotStart = slotStart; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public UUID getSlotId() { return slotId; }
    public void setSlotId(UUID slotId) { this.slotId = slotId; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    // Builder
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
        private String clientName;
        private String clientEmail;
        private String clientPhone;
        private Integer version;

        public BookingBuilder clientId(UUID clientId) { this.clientId = clientId; return this; }
        public BookingBuilder businessId(UUID businessId) { this.businessId = businessId; return this; }
        public BookingBuilder serviceId(UUID serviceId) { this.serviceId = serviceId; return this; }
        public BookingBuilder staffId(UUID staffId) { this.staffId = staffId; return this; }
        public BookingBuilder slotStart(Instant slotStart) { this.slotStart = slotStart; return this; }
        public BookingBuilder duration(Integer duration) { this.duration = duration; return this; }
        public BookingBuilder slotId(UUID slotId) { this.slotId = slotId; return this; }
        public BookingBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingBuilder clientName(String clientName) { this.clientName = clientName; return this; }
        public BookingBuilder clientEmail(String clientEmail) { this.clientEmail = clientEmail; return this; }
        public BookingBuilder clientPhone(String clientPhone) { this.clientPhone = clientPhone; return this; }
        public BookingBuilder version(Integer version) { this.version = version; return this; }

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
            booking.clientName = this.clientName;
            booking.clientEmail = this.clientEmail;
            booking.clientPhone = this.clientPhone;
            booking.version = this.version;
            return booking;
        }
    }

    public static BookingBuilder builder() { return new BookingBuilder(); }
}