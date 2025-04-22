/*package com.CheritSolutions.Booking_Microservice.Entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Servicee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer duration; // Duration in minutes (e.g., 60)

    @Column(nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2) // Decimal precision
    private BigDecimal basePrice;

    

  
    }*/