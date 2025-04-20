package com.CheritSolutions.Booking_Microservice.repositories;

import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    Optional<Feedback> findByBookingId(UUID bookingId);
    Optional<Feedback> findByBookingIdAndClientId(UUID bookingId, UUID clientId);
}