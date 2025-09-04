package com.CheritSolutions.Booking_Microservice.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;
import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;
import com.CheritSolutions.Booking_Microservice.dto.AvailableSlotDTO;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.DashboardResponse;
import com.CheritSolutions.Booking_Microservice.dto.SentimentResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;
import com.CheritSolutions.Booking_Microservice.dto.TrendResponse;
import com.CheritSolutions.Booking_Microservice.repositories.BookingRepository;
import com.CheritSolutions.Booking_Microservice.repositories.FeedbackRepository;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class DashboardService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BusinessServiceClient businessServiceClient;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter DATE_RANGE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.systemDefault());

    @Transactional(readOnly = true)
    public DashboardResponse getDashboardMetrics(UUID businessId, String ownerId, Instant startDate, Instant endDate) {
        // Ensure startDate and endDate are effectively final
        final Instant finalStartDate = startDate != null ? startDate : Instant.now().minus(30, ChronoUnit.DAYS);
        final Instant finalEndDate = endDate != null ? endDate : Instant.now().plus(30, ChronoUnit.DAYS);

        // Validate business ownership
        BusinessResponse business = businessServiceClient.getBusinessById(businessId);
        if (!business.getId().equals(businessId) || !business.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Unauthorized access to business");
        }

        // Fetch booking metrics
        Long totalBookings = bookingRepository.countTotalBookings(businessId);
        Long confirmedBookings = bookingRepository.countBookingsByStatus(businessId, BookingStatus.CONFIRMED);
        Long finishedBookings = bookingRepository.countBookingsByStatus(businessId, BookingStatus.FINISHED);
        Long cancelledBookings = bookingRepository.countBookingsByStatus(businessId, BookingStatus.CANCELLED);
        Long repeatCustomerCount = bookingRepository.countRepeatCustomers(businessId);
        Long uniqueCustomerCount = bookingRepository.countUniqueCustomers(businessId);

        // Calculate new customer count and churn rate
        Long newCustomerCount = uniqueCustomerCount - (repeatCustomerCount != null ? repeatCustomerCount : 0L);
        Double customerChurnRate = uniqueCustomerCount > 0 ?
            (double) (uniqueCustomerCount - repeatCustomerCount) / uniqueCustomerCount * 100 : 0.0;

        // Calculate cancellation rate
        Double cancellationRate = totalBookings > 0 ?
            (double) cancelledBookings / totalBookings * 100 : 0.0;

        // Calculate conversion rate
        Double conversionRate = totalBookings > 0 ?
            (double) (confirmedBookings + finishedBookings) / totalBookings * 100 : 0.0;

        // Fetch services and staff
        List<ServiceResponse> services = businessServiceClient.getServicesByBusiness(businessId);
        List<StaffResponse> staff = businessServiceClient.getAllStaffByBusiness(businessId).getBody();

        // Map service and staff IDs to details
        Map<UUID, ServiceResponse> serviceMap = services.stream()
            .collect(Collectors.toMap(ServiceResponse::getId, s -> s));
        Map<UUID, String> staffNames = staff.stream()
            .collect(Collectors.toMap(StaffResponse::getId, StaffResponse::getName));
        Map<UUID, Double> staffRatings = staff.stream()
            .collect(Collectors.toMap(
                StaffResponse::getId,
                s -> s.getAverageRating() != null ? s.getAverageRating().doubleValue() : 0.0
            ));

        // Calculate total revenue
        List<Booking> finishedBookingsList = bookingRepository.findByBusinessIdAndStatusAndSlotStartBetween(
            businessId, BookingStatus.FINISHED, finalStartDate, finalEndDate);
        BigDecimal totalRevenue = finishedBookingsList.stream()
            .map(b -> serviceMap.get(b.getServiceId()))
            .filter(s -> s != null && s.getBasePrice() != null)
            .map(ServiceResponse::getBasePrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate average revenue per booking and per customer
        BigDecimal averageRevenuePerBooking = finishedBookings > 0 ?
            totalRevenue.divide(new BigDecimal(finishedBookings), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal averageRevenuePerCustomer = uniqueCustomerCount > 0 ?
            totalRevenue.divide(new BigDecimal(uniqueCustomerCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        // Calculate revenue growth (compare to previous period)
        Instant prevStartDate = finalStartDate.minus(Duration.between(finalStartDate, finalEndDate));
        Instant prevEndDate = finalStartDate;
        List<Booking> prevFinishedBookings = bookingRepository.findByBusinessIdAndStatusAndSlotStartBetween(
            businessId, BookingStatus.FINISHED, prevStartDate, prevEndDate);
        BigDecimal prevTotalRevenue = prevFinishedBookings.stream()
            .map(b -> serviceMap.get(b.getServiceId()))
            .filter(s -> s != null && s.getBasePrice() != null)
            .map(ServiceResponse::getBasePrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal revenueGrowthPercentage = prevTotalRevenue.compareTo(BigDecimal.ZERO) > 0 ?
            totalRevenue.subtract(prevTotalRevenue)
                .divide(prevTotalRevenue, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")) : BigDecimal.ZERO;

        // Fetch top services by bookings
        List<DashboardResponse.TopServiceDTO> topServicesByBookings = bookingRepository.findServiceBookingData(businessId)
            .stream()
            .map(obj -> {
                UUID serviceId = (UUID) obj[0];
                Long bookingCount = (Long) obj[1];
                ServiceResponse service = serviceMap.get(serviceId);
                BigDecimal revenue = finishedBookingsList.stream()
                    .filter(b -> b.getServiceId().equals(serviceId))
                    .map(b -> serviceMap.get(b.getServiceId()).getBasePrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                return new DashboardResponse.TopServiceDTO(
                    serviceId,
                    service != null ? service.getName() : "Unknown",
                    bookingCount,
                    revenue
                );
            })
            .sorted((a, b) -> b.getBookingCount().compareTo(a.getBookingCount()))
            .limit(5)
            .collect(Collectors.toList());

        // Fetch top services by revenue
        List<DashboardResponse.TopServiceDTO> topServicesByRevenue = finishedBookingsList.stream()
            .collect(Collectors.groupingBy(
                Booking::getServiceId,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    b -> serviceMap.get(b.getServiceId()) != null ? serviceMap.get(b.getServiceId()).getBasePrice() : BigDecimal.ZERO,
                    BigDecimal::add
                )
            ))
            .entrySet().stream()
            .map(entry -> {
                UUID serviceId = entry.getKey();
                BigDecimal revenue = entry.getValue();
                Long bookingCount = finishedBookingsList.stream()
                    .filter(b -> b.getServiceId().equals(serviceId))
                    .count();
                ServiceResponse service = serviceMap.get(serviceId);
                return new DashboardResponse.TopServiceDTO(
                    serviceId,
                    service != null ? service.getName() : "Unknown",
                    bookingCount,
                    revenue
                );
            })
            .sorted((a, b) -> b.getTotalRevenue().compareTo(a.getTotalRevenue()))
            .limit(5)
            .collect(Collectors.toList());

        // Fetch top staff by bookings
        List<DashboardResponse.TopStaffDTO> topStaffByBookings = bookingRepository.findStaffBookingData(businessId)
            .stream()
            .map(obj -> {
                UUID staffId = (UUID) obj[0];
                Long bookingCount = (Long) obj[1];
                return new DashboardResponse.TopStaffDTO(
                    staffId,
                    staffNames.getOrDefault(staffId, "Unknown"),
                    bookingCount,
                    staffRatings.getOrDefault(staffId, 0.0)
                );
            })
            .sorted((a, b) -> b.getBookingCount().compareTo(a.getBookingCount()))
            .limit(5)
            .collect(Collectors.toList());

        // Fetch peak hours
        List<DashboardResponse.PeakHourDTO> peakHours = bookingRepository.findPeakHours(businessId, finalStartDate, finalEndDate)
            .stream()
            .map(row -> new DashboardResponse.PeakHourDTO(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).longValue()
            ))
            .limit(5)
            .collect(Collectors.toList());

        // Fetch peak days
        List<DashboardResponse.PeakDayDTO> peakDays = bookingRepository.findPeakDays(businessId, finalStartDate, finalEndDate)
            .stream()
            .map(row -> new DashboardResponse.PeakDayDTO(
                ((Number) row[0]).intValue() + 1, // Adjust DOW to 1=Sunday
                ((Number) row[1]).longValue()
            ))
            .limit(5)
            .collect(Collectors.toList());

        // Fetch peak months
        List<DashboardResponse.PeakMonthDTO> peakMonths = bookingRepository.findPeakMonths(businessId, finalStartDate, finalEndDate)
            .stream()
            .map(row -> new DashboardResponse.PeakMonthDTO(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).longValue()
            ))
            .limit(3)
            .collect(Collectors.toList());

        // Calculate staff utilization
        List<DashboardResponse.StaffUtilizationDTO> staffUtilization = staff.stream()
            .map(s -> {
                List<AvailableSlotDTO> slots = businessServiceClient.getAllStaffSlots(businessId, s.getId(), finalStartDate, finalEndDate);
                long bookedDuration = slots.stream()
                    .filter(slot -> slot.getStatus().equals("BOOKED"))
                    .mapToLong(AvailableSlotDTO::getDuration)
                    .sum();
                long totalDuration = calculateTotalAvailableDuration(s.getSchedule(), finalStartDate, finalEndDate);
                double utilization = totalDuration > 0 ? (double) bookedDuration / totalDuration * 100 : 0.0;
                return new DashboardResponse.StaffUtilizationDTO(s.getId(), s.getName(), utilization);
            })
            .collect(Collectors.toList());

        // Calculate average staff utilization
        Double averageStaffUtilization = staffUtilization.stream()
            .mapToDouble(DashboardResponse.StaffUtilizationDTO::getUtilizationPercentage)
            .average()
            .orElse(0.0);

        // Calculate feedback sentiment
        List<Feedback> feedbacks = feedbackRepository.findByBusinessId(businessId);
        long positiveComments = feedbacks.stream()
            .filter(f -> isPositiveComment(f.getServiceComment()) || isPositiveComment(f.getStaffComment()))
            .count();
        long negativeComments = feedbacks.stream()
            .filter(f -> isNegativeComment(f.getServiceComment()) || isNegativeComment(f.getStaffComment()))
            .count();
        long totalFeedbackCount = feedbacks.size();
        double positivePercentage = (positiveComments + negativeComments) > 0 ?
            (double) positiveComments / (positiveComments + negativeComments) * 100 : 0.0;
        SentimentResponse sentiment = new SentimentResponse(positiveComments, negativeComments, totalFeedbackCount, positivePercentage);

        // Calculate feedback rate
        Long finishedBookingsWithFeedback = bookingRepository.countFinishedBookingsWithFeedback(businessId);
        Double feedbackRate = finishedBookings > 0 ?
            (double) finishedBookingsWithFeedback / finishedBookings * 100 : 0.0;

        // Calculate staff revenue
        List<DashboardResponse.StaffRevenueDTO> staffRevenue = finishedBookingsList.stream()
            .collect(Collectors.groupingBy(
                Booking::getStaffId,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    b -> serviceMap.get(b.getServiceId()) != null ? serviceMap.get(b.getServiceId()).getBasePrice() : BigDecimal.ZERO,
                    BigDecimal::add
                )
            ))
            .entrySet().stream()
            .map(entry -> new DashboardResponse.StaffRevenueDTO(
                entry.getKey(),
                staffNames.getOrDefault(entry.getKey(), "Unknown"),
                entry.getValue()
            ))
            .sorted((a, b) -> b.getTotalRevenue().compareTo(a.getTotalRevenue()))
            .collect(Collectors.toList());

        // Calculate per-service metrics
        List<DashboardResponse.ServiceMetricsDTO> serviceMetrics = services.stream()
            .map(service -> {
                UUID serviceId = service.getId();
                Long serviceTotalBookings = bookingRepository.countTotalByService(serviceId, businessId);
                Long serviceFinishedBookings = bookingRepository.countByServiceAndStatus(serviceId, businessId, BookingStatus.FINISHED);
                Long serviceCancelledBookings = bookingRepository.countByServiceAndStatus(serviceId, businessId, BookingStatus.CANCELLED);
                Long serviceRepeatBookings = bookingRepository.countRepeatByService(serviceId, businessId);
                BigDecimal serviceRevenue = finishedBookingsList.stream()
                    .filter(b -> b.getServiceId().equals(serviceId))
                    .map(b -> serviceMap.get(b.getServiceId()).getBasePrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                Double serviceCancellationRate = serviceTotalBookings > 0 ?
                    (double) serviceCancelledBookings / serviceTotalBookings * 100 : 0.0;
                
                Double serviceRevenueShare = totalRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                    serviceRevenue.divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue() : 0.0;
                Long serviceFeedbackCount = bookingRepository.countFinishedWithFeedbackByService(serviceId, businessId);
                Double serviceFeedbackRate = serviceFinishedBookings > 0 ?
                    (double) serviceFeedbackCount / serviceFinishedBookings * 100 : 0.0;
                Double serviceAvgDuration = bookingRepository.findAverageDurationByService(serviceId, businessId);
                List<Feedback> serviceFeedbacks = feedbackRepository.findByBusinessIdAndServiceId(businessId, serviceId);
                long servicePositiveComments = serviceFeedbacks.stream()
                    .filter(f -> isPositiveComment(f.getServiceComment()) || isPositiveComment(f.getStaffComment()))
                    .count();
                long serviceNegativeComments = serviceFeedbacks.stream()
                    .filter(f -> isNegativeComment(f.getServiceComment()) || isNegativeComment(f.getStaffComment()))
                    .count();
                long serviceTotalFeedbackCount = serviceFeedbacks.size();
                double servicePositivePercentage = (servicePositiveComments + serviceNegativeComments) > 0 ?
                    (double) servicePositiveComments / (servicePositiveComments + serviceNegativeComments) * 100 : 0.0;
                SentimentResponse serviceSentiment = new SentimentResponse(
                    servicePositiveComments, serviceNegativeComments, serviceTotalFeedbackCount, servicePositivePercentage);
                return new DashboardResponse.ServiceMetricsDTO(
                    serviceId, service.getName(), serviceTotalBookings, serviceFinishedBookings,
                    serviceCancelledBookings, serviceRevenue, serviceCancellationRate, serviceRevenueShare,
                    serviceRepeatBookings != null ? serviceRepeatBookings : 0L, // Fix: Handle null repeatBookings
                    serviceSentiment, serviceFeedbackRate, serviceAvgDuration
                );
            })
            .collect(Collectors.toList());

        // Create date range summary
        String dateRangeSummary = String.format("Metrics from %s to %s",
            DATE_RANGE_FORMATTER.format(finalStartDate),
            DATE_RANGE_FORMATTER.format(finalEndDate));

        return new DashboardResponse(
            totalBookings, confirmedBookings, finishedBookings, cancelledBookings,
            totalRevenue, cancellationRate, repeatCustomerCount != null ? repeatCustomerCount : 0L,
            topServicesByBookings, topServicesByRevenue, topStaffByBookings,
            peakHours, peakDays, peakMonths, staffUtilization, sentiment, staffRevenue,
            feedbackRate, averageRevenuePerBooking, averageRevenuePerCustomer,
            newCustomerCount, customerChurnRate, revenueGrowthPercentage, conversionRate,
            averageStaffUtilization, serviceMetrics, dateRangeSummary
        );
    }

    @Transactional(readOnly = true)
    public TrendResponse getBookingTrends(UUID businessId, String ownerId, Instant startDate, Instant endDate) {
        BusinessResponse business = businessServiceClient.getBusinessById(businessId);
        if (!business.getId().equals(businessId) || !business.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Unauthorized access to business");
        }

        final Instant finalStartDate = startDate != null ? startDate : Instant.now().minus(30, ChronoUnit.DAYS);
        final Instant finalEndDate = endDate != null ? endDate : Instant.now();

        List<Object[]> rawTrendData = bookingRepository.findDailyBookingTrends(businessId, finalStartDate, finalEndDate);

        List<Booking> finishedBookingsInRange = bookingRepository.findByBusinessIdAndStatusAndSlotStartBetween(
            businessId, BookingStatus.FINISHED, finalStartDate, finalEndDate);

        List<ServiceResponse> services = businessServiceClient.getServicesByBusiness(businessId);
        Map<UUID, ServiceResponse> serviceMap = services.stream()
            .collect(Collectors.toMap(ServiceResponse::getId, s -> s));

        Map<UUID, BigDecimal> bookingRevenueMap = new HashMap<>();
        for (Booking booking : finishedBookingsInRange) {
            ServiceResponse service = serviceMap.get(booking.getServiceId());
            BigDecimal price = (service != null && service.getBasePrice() != null) ? service.getBasePrice() : BigDecimal.ZERO;
            bookingRevenueMap.put(booking.getId(), price);
        }

        Map<String, BigDecimal> dailyRevenueMap = finishedBookingsInRange.stream()
            .collect(Collectors.groupingBy(
                booking -> booking.getSlotStart().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                Collectors.reducing(
                    BigDecimal.ZERO,
                    booking -> bookingRevenueMap.getOrDefault(booking.getId(), BigDecimal.ZERO),
                    BigDecimal::add
                )
            ));

        List<TrendResponse.DailyTrendDTO> dailyTrends = rawTrendData.stream()
            .map(row -> {
                String bookingDateStr = (String) row[0];
                Long bookingCount = ((Number) row[1]).longValue();
                BigDecimal revenue = dailyRevenueMap.getOrDefault(bookingDateStr, BigDecimal.ZERO);
                return new TrendResponse.DailyTrendDTO(bookingDateStr, bookingCount, revenue);
            })
            .collect(Collectors.toList());

        return new TrendResponse(dailyTrends);
    }

    private long calculateTotalAvailableDuration(JsonNode schedule, Instant startDate, Instant endDate) {
        if (schedule == null) {
            return 0;
        }

        long totalMinutes = 0;
        LocalDate start = startDate.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
            JsonNode daySchedule = schedule.get(dayName);
            if (daySchedule == null || daySchedule.asText().equals("Closed")) {
                continue;
            }

            String[] hours = daySchedule.asText().split(" - ");
            try {
                LocalTime startTime = LocalTime.parse(hours[0], TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(hours[1], TIME_FORMATTER);
                long hoursDuration = Duration.between(startTime, endTime).toMinutes();
                if (hoursDuration > 0) {
                    totalMinutes += hoursDuration;
                }
            } catch (Exception e) {
                System.err.println("Error parsing schedule for " + dayName + ": " + daySchedule.asText());
            }
        }

        return totalMinutes;
    }

    private boolean isPositiveComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) return false;
        String lowerComment = comment.toLowerCase();
        List<String> positiveKeywords = Arrays.asList("great", "excellent", "amazing", "wonderful", "good", "satisfied", "happy", "fantastic", "awesome");
        return positiveKeywords.stream().anyMatch(lowerComment::contains);
    }

    private boolean isNegativeComment(String comment) {
        if (comment == null || comment.trim().isEmpty()) return false;
        String lowerComment = comment.toLowerCase();
        List<String> negativeKeywords = Arrays.asList("bad", "poor", "terrible", "awful", "disappointed", "unhappy", "horrible", "worst");
        return negativeKeywords.stream().anyMatch(lowerComment::contains);
    }
}