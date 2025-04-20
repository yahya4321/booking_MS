// src/main/java/com/CheritSolutions/Booking_Microservice/repositories/BookingRepository.java
package com.CheritSolutions.Booking_Microservice.repositories;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByClientId(UUID clientId);

    Optional<Booking> findByIdAndClientId(UUID bookingId, UUID clientId);

}