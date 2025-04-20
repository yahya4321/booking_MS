package com.CheritSolutions.Booking_Microservice.dto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter 
@Setter 
public class StaffResponse {
    private UUID id;
    private String name;
    private String position;
    private Integer postBufferTime;
    private UUID businessId;   
    private JsonNode schedule;
    private List<AvailableSlotDTO> availabilitySlots; 
    private UUID serviceId; 

    
}