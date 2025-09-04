package com.CheritSolutions.Booking_Microservice.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByClientId(UUID clientId);

    Optional<Booking> findByIdAndClientId(UUID bookingId, UUID clientId);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByBusinessIdAndStatus(UUID businessId, BookingStatus status, Pageable pageable);

    Page<Booking> findByBusinessId(UUID businessId, Pageable pageable);

    List<Booking> findByStaffId(UUID staffId);

    Page<Booking> findByStaffId(UUID staffId, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.businessId = :businessId")
    Long countTotalBookings(@Param("businessId") UUID businessId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.businessId = :businessId AND b.status = :status")
    Long countBookingsByStatus(@Param("businessId") UUID businessId, @Param("status") BookingStatus status);

    @Query("SELECT b.serviceId, COUNT(b) as bookingCount FROM Booking b " +
           "WHERE b.businessId = :businessId AND b.status = 'FINISHED' " +
           "GROUP BY b.serviceId")
    List<Object[]> findServiceBookingData(@Param("businessId") UUID businessId);

    @Query("SELECT b.staffId, COUNT(b) as bookingCount FROM Booking b " +
           "WHERE b.businessId = :businessId AND b.status = 'FINISHED' " +
           "GROUP BY b.staffId")
    List<Object[]> findStaffBookingData(@Param("businessId") UUID businessId);

    @Query("SELECT COUNT(DISTINCT b.clientId) FROM Booking b WHERE b.businessId = :businessId " +
           "AND b.status = 'FINISHED' GROUP BY b.clientId HAVING COUNT(b) > 1")
    Long countRepeatCustomers(@Param("businessId") UUID businessId);

    List<Booking> findByBusinessIdAndStatusAndSlotStartBetween(UUID businessId, BookingStatus status, Instant startDate, Instant endDate);

    @Query(value = "SELECT TO_CHAR(slot_start, 'YYYY-MM-DD') AS booking_date, " +
           "COUNT(id) AS booking_count " +
           "FROM booking " +
           "WHERE business_id = :businessId AND status = 'FINISHED' " +
           "AND slot_start BETWEEN :startDate AND :endDate " +
           "GROUP BY TO_CHAR(slot_start, 'YYYY-MM-DD') " +
           "ORDER BY TO_CHAR(slot_start, 'YYYY-MM-DD')", nativeQuery = true)
    List<Object[]> findDailyBookingTrends(@Param("businessId") UUID businessId,
                                         @Param("startDate") Instant startDate,
                                         @Param("endDate") Instant endDate);

    @Query(value = "SELECT EXTRACT(HOUR FROM slot_start) AS hour_of_day, COUNT(id) AS booking_count " +
           "FROM booking " +
           "WHERE business_id = :businessId AND status = 'FINISHED' " +
           "AND slot_start BETWEEN :startDate AND :endDate " +
           "GROUP BY EXTRACT(HOUR FROM slot_start) " +
           "HAVING COUNT(id) > 1 " +
           "ORDER BY COUNT(id) DESC", nativeQuery = true)
    List<Object[]> findPeakHours(@Param("businessId") UUID businessId,
                                 @Param("startDate") Instant startDate,
                                 @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(b) FROM Booking b " +
           "WHERE b.businessId = :businessId AND b.status = 'FINISHED' " +
           "AND EXISTS (SELECT 1 FROM Feedback f WHERE f.booking = b)")
    Long countFinishedBookingsWithFeedback(@Param("businessId") UUID businessId);

    boolean existsByClientIdAndStaffId(UUID clientId, UUID staffId);

    // New query: Peak Days
    @Query(value = "SELECT EXTRACT(DOW FROM slot_start) AS day_of_week, COUNT(id) AS booking_count " +
           "FROM booking " +
           "WHERE business_id = :businessId AND status = 'FINISHED' " +
           "AND slot_start BETWEEN :startDate AND :endDate " +
           "GROUP BY EXTRACT(DOW FROM slot_start) " +
           "ORDER BY booking_count DESC", nativeQuery = true)
    List<Object[]> findPeakDays(@Param("businessId") UUID businessId,
                                @Param("startDate") Instant startDate,
                                @Param("endDate") Instant endDate);

    // New query: Peak Months
    @Query(value = "SELECT EXTRACT(MONTH FROM slot_start) AS month, COUNT(id) AS booking_count " +
           "FROM booking " +
           "WHERE business_id = :businessId AND status = 'FINISHED' " +
           "AND slot_start BETWEEN :startDate AND :endDate " +
           "GROUP BY EXTRACT(MONTH FROM slot_start) " +
           "ORDER BY booking_count DESC", nativeQuery = true)
    List<Object[]> findPeakMonths(@Param("businessId") UUID businessId,
                                  @Param("startDate") Instant startDate,
                                  @Param("endDate") Instant endDate);

    // New query: Total bookings per service
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceId = :serviceId AND b.businessId = :businessId")
    Long countTotalByService(@Param("serviceId") UUID serviceId, @Param("businessId") UUID businessId);

    // New query: Bookings per service by status
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceId = :serviceId AND b.businessId = :businessId AND b.status = :status")
    Long countByServiceAndStatus(@Param("serviceId") UUID serviceId, @Param("businessId") UUID businessId, @Param("status") BookingStatus status);

    // New query: Repeat bookings per service
    @Query("SELECT COUNT(DISTINCT b.clientId) FROM Booking b WHERE b.serviceId = :serviceId AND b.businessId = :businessId " +
           "AND b.status = 'FINISHED' GROUP BY b.clientId HAVING COUNT(b) > 1")
    Long countRepeatByService(@Param("serviceId") UUID serviceId, @Param("businessId") UUID businessId);

    // New query: Feedback count per service
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceId = :serviceId AND b.businessId = :businessId AND b.status = 'FINISHED' " +
           "AND EXISTS (SELECT 1 FROM Feedback f WHERE f.booking = b)")
    Long countFinishedWithFeedbackByService(@Param("serviceId") UUID serviceId, @Param("businessId") UUID businessId);

    // New query: Unique customers
    @Query("SELECT COUNT(DISTINCT b.clientId) FROM Booking b WHERE b.businessId = :businessId AND b.status = 'FINISHED'")
    Long countUniqueCustomers(@Param("businessId") UUID businessId);

    // New query: Average duration per service
    @Query("SELECT AVG(b.duration) FROM Booking b WHERE b.serviceId = :serviceId AND b.businessId = :businessId AND b.status = 'FINISHED'")
    Double findAverageDurationByService(@Param("serviceId") UUID serviceId, @Param("businessId") UUID businessId);
}