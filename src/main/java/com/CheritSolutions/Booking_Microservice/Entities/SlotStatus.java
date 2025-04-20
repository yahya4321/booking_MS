package com.CheritSolutions.Booking_Microservice.Entities;

public enum SlotStatus {
    AVAILABLE, // Green slot (open for booking)
    BOOKED,    // Red zone (actual booking time)
    REST       // Buffer time (15 mins before/after bookings)
}