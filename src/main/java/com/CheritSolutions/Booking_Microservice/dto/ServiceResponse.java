package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.Setter;

@Getter // Add this
@Setter // Add this
public class ServiceResponse {
    private UUID id;
    private String name;
    private BigDecimal basePrice; // Calculated from base_price + pricing_rules
    private JsonNode pricingRules; // Simplified JSON for clients
    private UUID businessId; // Add this field
    private Integer duration;
    private List<UUID> staffIds; // Populated from staff list IDs
}
