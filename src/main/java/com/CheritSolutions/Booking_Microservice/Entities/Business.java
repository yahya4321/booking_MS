package com.CheritSolutions.Booking_Microservice.Entities;


import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use UUID for distributed systems
    private UUID id;

    @Column(nullable = false, unique = true) // Ensure business names are unique
    private String name;

    @Column(nullable = false)
    private String address;
    
   

    

    
}