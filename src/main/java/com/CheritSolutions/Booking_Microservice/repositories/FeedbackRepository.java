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


    @Query("SELECT f FROM Feedback f WHERE f.booking.businessId = :businessId AND f.booking.serviceId = :serviceId")
    List<Feedback> findByBusinessIdAndServiceId(@Param("businessId") UUID businessId, @Param("serviceId") UUID serviceId);
}