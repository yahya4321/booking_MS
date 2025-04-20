package com.CheritSolutions.Booking_Microservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
@NoArgsConstructor // Add this for Hibernate
@AllArgsConstructor // Add this for 
@Entity
@Getter
@Setter
@Builder
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
}