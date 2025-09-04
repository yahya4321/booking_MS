package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class DashboardResponse {
    private Long totalBookings;
    private Long confirmedBookings;
    private Long finishedBookings;
    private Long cancelledBookings;
    private BigDecimal totalRevenue;
    private Double cancellationRate;
    private Long repeatCustomerCount;
    private List<TopServiceDTO> topServicesByBookings;
    private List<TopServiceDTO> topServicesByRevenue;
    private List<TopStaffDTO> topStaffByBookings;
    private List<PeakHourDTO> peakHours;
    private List<PeakDayDTO> peakDays; // New: Peak days for better operational planning
    private List<PeakMonthDTO> peakMonths; // New: Peak months for seasonal insights
    private List<StaffUtilizationDTO> staffUtilization;
    private SentimentResponse feedbackSentiment;
    private Double feedbackRate;
    private List<StaffRevenueDTO> staffRevenue;
    private BigDecimal averageRevenuePerBooking; // New: Average revenue per booking for efficiency
    private BigDecimal averageRevenuePerCustomer; // New: Average revenue per customer for value assessment
    private Long newCustomerCount; // New: New customers for growth tracking
    private Double customerChurnRate; // New: Churn rate for retention insights
    private BigDecimal revenueGrowthPercentage; // New: Period-over-period revenue growth
    private Double conversionRate; // New: Booking conversion rate
    private Double averageStaffUtilization; // New: Overall staff utilization average
    private List<ServiceMetricsDTO> serviceMetrics; // New: Per-service detailed metrics
    private String dateRangeSummary; // New: Summary of the date range for clarity

    public DashboardResponse() {}

    // Updated all-args constructor with new fields
    public DashboardResponse(Long totalBookings, Long confirmedBookings, Long finishedBookings,
                            Long cancelledBookings, BigDecimal totalRevenue, Double cancellationRate,
                            Long repeatCustomerCount, List<TopServiceDTO> topServicesByBookings,
                            List<TopServiceDTO> topServicesByRevenue, List<TopStaffDTO> topStaffByBookings,
                            List<PeakHourDTO> peakHours, List<PeakDayDTO> peakDays, List<PeakMonthDTO> peakMonths,
                            List<StaffUtilizationDTO> staffUtilization,
                            SentimentResponse feedbackSentiment, List<StaffRevenueDTO> staffRevenue, Double feedbackRate,
                            BigDecimal averageRevenuePerBooking, BigDecimal averageRevenuePerCustomer,
                            Long newCustomerCount, Double customerChurnRate, BigDecimal revenueGrowthPercentage,
                            Double conversionRate, Double averageStaffUtilization,
                            List<ServiceMetricsDTO> serviceMetrics, String dateRangeSummary) {

        this.totalBookings = totalBookings;
        this.confirmedBookings = confirmedBookings;
        this.finishedBookings = finishedBookings;
        this.cancelledBookings = cancelledBookings;
        this.totalRevenue = totalRevenue;
        this.cancellationRate = cancellationRate;
        this.repeatCustomerCount = repeatCustomerCount;
        this.topServicesByBookings = topServicesByBookings;
        this.topServicesByRevenue = topServicesByRevenue;
        this.topStaffByBookings = topStaffByBookings;
        this.peakHours = peakHours;
        this.peakDays = peakDays;
        this.peakMonths = peakMonths;
        this.staffUtilization = staffUtilization;
        this.feedbackSentiment = feedbackSentiment;
        this.feedbackRate = feedbackRate;
        this.staffRevenue = staffRevenue;
        this.averageRevenuePerBooking = averageRevenuePerBooking;
        this.averageRevenuePerCustomer = averageRevenuePerCustomer;
        this.newCustomerCount = newCustomerCount;
        this.customerChurnRate = customerChurnRate;
        this.revenueGrowthPercentage = revenueGrowthPercentage;
        this.conversionRate = conversionRate;
        this.averageStaffUtilization = averageStaffUtilization;
        this.serviceMetrics = serviceMetrics;
        this.dateRangeSummary = dateRangeSummary;
    }

    // Getters and Setters
    public Long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }
    public Long getConfirmedBookings() { return confirmedBookings; }
    public void setConfirmedBookings(Long confirmedBookings) { this.confirmedBookings = confirmedBookings; }
    public Long getFinishedBookings() { return finishedBookings; }
    public void setFinishedBookings(Long finishedBookings) { this.finishedBookings = finishedBookings; }
    public Long getCancelledBookings() { return cancelledBookings; }
    public void setCancelledBookings(Long cancelledBookings) { this.cancelledBookings = cancelledBookings; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public Double getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(Double cancellationRate) { this.cancellationRate = cancellationRate; }
    public Long getRepeatCustomerCount() { return repeatCustomerCount; }
    public void setRepeatCustomerCount(Long repeatCustomerCount) { this.repeatCustomerCount = repeatCustomerCount; }
    public List<TopServiceDTO> getTopServicesByBookings() { return topServicesByBookings; }
    public void setTopServicesByBookings(List<TopServiceDTO> topServicesByBookings) { this.topServicesByBookings = topServicesByBookings; }
    public List<TopServiceDTO> getTopServicesByRevenue() { return topServicesByRevenue; }
    public void setTopServicesByRevenue(List<TopServiceDTO> topServicesByRevenue) { this.topServicesByRevenue = topServicesByRevenue; }
    public List<TopStaffDTO> getTopStaffByBookings() { return topStaffByBookings; }
    public void setTopStaffByBookings(List<TopStaffDTO> topStaffByBookings) { this.topStaffByBookings = topStaffByBookings; }
    public List<PeakHourDTO> getPeakHours() { return peakHours; }
    public void setPeakHours(List<PeakHourDTO> peakHours) { this.peakHours = peakHours; }
    public List<PeakDayDTO> getPeakDays() { return peakDays; }
    public void setPeakDays(List<PeakDayDTO> peakDays) { this.peakDays = peakDays; }
    public List<PeakMonthDTO> getPeakMonths() { return peakMonths; }
    public void setPeakMonths(List<PeakMonthDTO> peakMonths) { this.peakMonths = peakMonths; }
    public List<StaffUtilizationDTO> getStaffUtilization() { return staffUtilization; }
    public void setStaffUtilization(List<StaffUtilizationDTO> staffUtilization) { this.staffUtilization = staffUtilization; }
    public SentimentResponse getFeedbackSentiment() { return feedbackSentiment; }
    public void setFeedbackSentiment(SentimentResponse feedbackSentiment) { this.feedbackSentiment = feedbackSentiment; }
    public Double getFeedbackRate() { return feedbackRate; }
    public void setFeedbackRate(Double feedbackRate) { this.feedbackRate = feedbackRate; }
    public List<StaffRevenueDTO> getStaffRevenue() { return staffRevenue; }
    public void setStaffRevenue(List<StaffRevenueDTO> staffRevenue) { this.staffRevenue = staffRevenue; }
    public BigDecimal getAverageRevenuePerBooking() { return averageRevenuePerBooking; }
    public void setAverageRevenuePerBooking(BigDecimal averageRevenuePerBooking) { this.averageRevenuePerBooking = averageRevenuePerBooking; }
    public BigDecimal getAverageRevenuePerCustomer() { return averageRevenuePerCustomer; }
    public void setAverageRevenuePerCustomer(BigDecimal averageRevenuePerCustomer) { this.averageRevenuePerCustomer = averageRevenuePerCustomer; }
    public Long getNewCustomerCount() { return newCustomerCount; }
    public void setNewCustomerCount(Long newCustomerCount) { this.newCustomerCount = newCustomerCount; }
    public Double getCustomerChurnRate() { return customerChurnRate; }
    public void setCustomerChurnRate(Double customerChurnRate) { this.customerChurnRate = customerChurnRate; }
    public BigDecimal getRevenueGrowthPercentage() { return revenueGrowthPercentage; }
    public void setRevenueGrowthPercentage(BigDecimal revenueGrowthPercentage) { this.revenueGrowthPercentage = revenueGrowthPercentage; }
    public Double getConversionRate() { return conversionRate; }
    public void setConversionRate(Double conversionRate) { this.conversionRate = conversionRate; }
    public Double getAverageStaffUtilization() { return averageStaffUtilization; }
    public void setAverageStaffUtilization(Double averageStaffUtilization) { this.averageStaffUtilization = averageStaffUtilization; }
    public List<ServiceMetricsDTO> getServiceMetrics() { return serviceMetrics; }
    public void setServiceMetrics(List<ServiceMetricsDTO> serviceMetrics) { this.serviceMetrics = serviceMetrics; }
    public String getDateRangeSummary() { return dateRangeSummary; }
    public void setDateRangeSummary(String dateRangeSummary) { this.dateRangeSummary = dateRangeSummary; }

    // Restored nested class: TopServiceDTO
    public static class TopServiceDTO {
        private UUID serviceId;
        private String serviceName;
        private Long bookingCount;
        private BigDecimal totalRevenue;

        public TopServiceDTO() {}
        public TopServiceDTO(UUID serviceId, String serviceName, Long bookingCount, BigDecimal totalRevenue) {
            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.bookingCount = bookingCount;
            this.totalRevenue = totalRevenue;
        }

        public UUID getServiceId() { return serviceId; }
        public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    // Restored nested class: TopStaffDTO
    public static class TopStaffDTO {
        private UUID staffId;
        private String staffName;
        private Long bookingCount;
        private Double averageRating;

        public TopStaffDTO() {}
        public TopStaffDTO(UUID staffId, String staffName, Long bookingCount, Double averageRating) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.bookingCount = bookingCount;
            this.averageRating = averageRating;
        }

        public UUID getStaffId() { return staffId; }
        public void setStaffId(UUID staffId) { this.staffId = staffId; }
        public String getStaffName() { return staffName; }
        public void setStaffName(String staffName) { this.staffName = staffName; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    }

    // Restored nested class: PeakHourDTO
    public static class PeakHourDTO {
        private Integer hourOfDay;
        private Long bookingCount;

        public PeakHourDTO() {}
        public PeakHourDTO(Integer hourOfDay, Long bookingCount) {
            this.hourOfDay = hourOfDay;
            this.bookingCount = bookingCount;
        }

        public Integer getHourOfDay() { return hourOfDay; }
        public void setHourOfDay(Integer hourOfDay) { this.hourOfDay = hourOfDay; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
    }

    // Restored nested class: StaffUtilizationDTO
    public static class StaffUtilizationDTO {
        private UUID staffId;
        private String staffName;
        private Double utilizationPercentage;

        public StaffUtilizationDTO() {}
        public StaffUtilizationDTO(UUID staffId, String staffName, Double utilizationPercentage) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.utilizationPercentage = utilizationPercentage;
        }

        public UUID getStaffId() { return staffId; }
        public void setStaffId(UUID staffId) { this.staffId = staffId; }
        public String getStaffName() { return staffName; }
        public void setStaffName(String staffName) { this.staffName = staffName; }
        public Double getUtilizationPercentage() { return utilizationPercentage; }
        public void setUtilizationPercentage(Double utilizationPercentage) { this.utilizationPercentage = utilizationPercentage; }
    }

    // Restored nested class: StaffRevenueDTO
    public static class StaffRevenueDTO {
        private UUID staffId;
        private String staffName;
        private BigDecimal totalRevenue;

        public StaffRevenueDTO() {}
        public StaffRevenueDTO(UUID staffId, String staffName, BigDecimal totalRevenue) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.totalRevenue = totalRevenue;
        }

        public UUID getStaffId() { return staffId; }
        public void setStaffId(UUID staffId) { this.staffId = staffId; }
        public String getStaffName() { return staffName; }
        public void setStaffName(String staffName) { this.staffName = staffName; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    // New nested class: PeakDayDTO
    public static class PeakDayDTO {
        private Integer dayOfWeek; // 1 = Sunday, 2 = Monday, etc.
        private Long bookingCount;

        public PeakDayDTO() {}
        public PeakDayDTO(Integer dayOfWeek, Long bookingCount) {
            this.dayOfWeek = dayOfWeek;
            this.bookingCount = bookingCount;
        }

        public Integer getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
    }

    // New nested class: PeakMonthDTO
    public static class PeakMonthDTO {
        private Integer month; // 1 = January, 2 = February, etc.
        private Long bookingCount;

        public PeakMonthDTO() {}
        public PeakMonthDTO(Integer month, Long bookingCount) {
            this.month = month;
            this.bookingCount = bookingCount;
        }

        public Integer getMonth() { return month; }
        public void setMonth(Integer month) { this.month = month; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
    }

    // New nested class: ServiceMetricsDTO
    public static class ServiceMetricsDTO {
        private UUID serviceId;
        private String serviceName;
        private Long totalBookings;
        private Long finishedBookings;
        private Long cancelledBookings;
        private BigDecimal totalRevenue;
        private Double cancellationRate;
        private Double revenueShare;
        private Long repeatBookings;
        private SentimentResponse serviceSentiment;
        private Double feedbackRate;
        private Double averageDuration;

        public ServiceMetricsDTO() {}
        public ServiceMetricsDTO(UUID serviceId, String serviceName, Long totalBookings, Long finishedBookings,
                                 Long cancelledBookings, BigDecimal totalRevenue, Double cancellationRate,
                                 Double revenueShare, Long repeatBookings, SentimentResponse serviceSentiment,
                                 Double feedbackRate, Double averageDuration) {
            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.totalBookings = totalBookings;
            this.finishedBookings = finishedBookings;
            this.cancelledBookings = cancelledBookings;
            this.totalRevenue = totalRevenue;
            this.cancellationRate = cancellationRate;
            this.revenueShare = revenueShare;
            this.repeatBookings = repeatBookings;
            this.serviceSentiment = serviceSentiment;
            this.feedbackRate = feedbackRate;
            this.averageDuration = averageDuration;
        }

        public UUID getServiceId() { return serviceId; }
        public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public Long getTotalBookings() { return totalBookings; }
        public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }
        public Long getFinishedBookings() { return finishedBookings; }
        public void setFinishedBookings(Long finishedBookings) { this.finishedBookings = finishedBookings; }
        public Long getCancelledBookings() { return cancelledBookings; }
        public void setCancelledBookings(Long cancelledBookings) { this.cancelledBookings = cancelledBookings; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        public Double getCancellationRate() { return cancellationRate; }
        public void setCancellationRate(Double cancellationRate) { this.cancellationRate = cancellationRate; }
        public Double getRevenueShare() { return revenueShare; }
        public void setRevenueShare(Double revenueShare) { this.revenueShare = revenueShare; }
        public Long getRepeatBookings() { return repeatBookings; }
        public void setRepeatBookings(Long repeatBookings) { this.repeatBookings = repeatBookings; }
        public SentimentResponse getServiceSentiment() { return serviceSentiment; }
        public void setServiceSentiment(SentimentResponse serviceSentiment) { this.serviceSentiment = serviceSentiment; }
        public Double getFeedbackRate() { return feedbackRate; }
        public void setFeedbackRate(Double feedbackRate) { this.feedbackRate = feedbackRate; }
        public Double getAverageDuration() { return averageDuration; }
        public void setAverageDuration(Double averageDuration) { this.averageDuration = averageDuration; }
    }
}