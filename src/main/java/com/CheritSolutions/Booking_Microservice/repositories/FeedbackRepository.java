package com.CheritSolutions.Booking_Microservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CheritSolutions.Booking_Microservice.Entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    Optional<Feedback> findByBookingId(UUID bookingId);
    Optional<Feedback> findByBookingIdAndClientId(UUID bookingId, UUID clientId);

    @Query("SELECT f FROM Feedback f JOIN Booking b ON f.booking = b " +
           "WHERE b.businessId = :businessId")
    List<Feedback> findByBusinessId(@Param("businessId") UUID businessId);
}