package com.CheritSolutions.Booking_Microservice.Entities;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}