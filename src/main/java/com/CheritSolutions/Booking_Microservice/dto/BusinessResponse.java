package com.CheritSolutions.Booking_Microservice.dto;

import java.util.List;
import java.util.UUID;

public class BusinessResponse {
    private UUID id;
    private String name;
    private String address;
    private String email;
    // private List<ServiceResponse> services;
    // private List<StaffResponse> staff;

    // Default constructor
    public BusinessResponse() {}

    // Full constructor (optional, uncomment if needed)
    /*
    public BusinessResponse(UUID id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }
    */

    // Getters and setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Uncomment if you decide to include services and staff
    /*
    public List<ServiceResponse> getServices() {
        return services;
    }

    public void setServices(List<ServiceResponse> services) {
        this.services = services;
    }

    public List<StaffResponse> getStaff() {
        return staff;
    }

    public void setStaff(List<StaffResponse> staff) {
        this.staff = staff;
    }
    */
}
