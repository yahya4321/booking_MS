package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public class ServiceResponse {
     private UUID id;
    private String name;
    private BigDecimal basePrice;
    private Integer duration;
    private UUID businessId;
    private String description;
    private JsonNode pricingRules;
    private List<UUID> staffIds;
    private List<UUID> imageIds;
    private BigDecimal averageRating;
    private Integer ratingCount;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public JsonNode getPricingRules() {
        return pricingRules;
    }

    public void setPricingRules(JsonNode pricingRules) {
        this.pricingRules = pricingRules;
    }

    public List<UUID> getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(List<UUID> staffIds) {
        this.staffIds = staffIds;
    }

    public List<UUID> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<UUID> imageIds) {
        this.imageIds = imageIds;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}

