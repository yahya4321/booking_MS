package com.CheritSolutions.Booking_Microservice.dto;


import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter // Ensure this is present
@Setter
public class BusinessResponse {
    private UUID id;
    private String name;
    private String address;
    private String email;
 //   private List<ServiceResponse> services; // Nested DTO
   // private List<StaffResponse> staff; // Simplified staff data
}