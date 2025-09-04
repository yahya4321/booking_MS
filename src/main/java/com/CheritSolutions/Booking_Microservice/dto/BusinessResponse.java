package com.CheritSolutions.Booking_Microservice.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public class BusinessResponse {
    private UUID id;
    private String name;
    private String address;
    private String email;
    private String description;
    private String ownerId;
    private String phoneNumber;
    private JsonNode schedule;
    private Double latitude;
    private Double longitude;
    private List<UUID> categoryIds;
    private List<UUID> imageIds;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public JsonNode getSchedule() {
        return schedule;
    }

    public void setSchedule(JsonNode schedule) {
        this.schedule = schedule;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<UUID> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<UUID> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<UUID> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<UUID> imageIds) {
        this.imageIds = imageIds;
    }
}