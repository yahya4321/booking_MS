package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public class ServiceResponse {
    private UUID id;
    private String name;
    private BigDecimal basePrice;
    private JsonNode pricingRules;
    private UUID businessId;
    private Integer duration;
    private List<UUID> staffIds;

    // Default constructor
    public ServiceResponse() {
    }

    // All-args constructor
    public ServiceResponse(UUID id, String name, BigDecimal basePrice, JsonNode pricingRules,
                           UUID businessId, Integer duration, List<UUID> staffIds) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.pricingRules = pricingRules;
        this.businessId = businessId;
        this.duration = duration;
        this.staffIds = staffIds;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public JsonNode getPricingRules() {
        return pricingRules;
    }

    public void setPricingRules(JsonNode pricingRules) {
        this.pricingRules = pricingRules;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<UUID> getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(List<UUID> staffIds) {
        this.staffIds = staffIds;
    }

    // Optional: toString method for logging/debugging
    @Override
    public String toString() {
        return "ServiceResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", pricingRules=" + pricingRules +
                ", businessId=" + businessId +
                ", duration=" + duration +
                ", staffIds=" + staffIds +
                '}';
    }
}
