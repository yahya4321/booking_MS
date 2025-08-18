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
    private List<StaffUtilizationDTO> staffUtilization;
    private SentimentResponse feedbackSentiment;
    private Double feedbackRate; 
    private List<StaffRevenueDTO> staffRevenue; 
    

    public DashboardResponse() {}

    // All-args constructor
    public DashboardResponse(Long totalBookings, Long confirmedBookings, Long finishedBookings, 
                            Long cancelledBookings, BigDecimal totalRevenue, Double cancellationRate,
                            Long repeatCustomerCount, List<TopServiceDTO> topServicesByBookings,
                            List<TopServiceDTO> topServicesByRevenue, List<TopStaffDTO> topStaffByBookings,
                            List<PeakHourDTO> peakHours, List<StaffUtilizationDTO> staffUtilization,
                            SentimentResponse feedbackSentiment, List<StaffRevenueDTO> staffRevenue ,Double feedbackRate) {

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
        this.staffUtilization = staffUtilization;
        this.feedbackSentiment = feedbackSentiment;
        this.feedbackRate = feedbackRate;
        this.staffRevenue = staffRevenue;
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
    public List<StaffUtilizationDTO> getStaffUtilization() { return staffUtilization; }
    public void setStaffUtilization(List<StaffUtilizationDTO> staffUtilization) { this.staffUtilization = staffUtilization; }
    public SentimentResponse getFeedbackSentiment() { return feedbackSentiment; }
    public void setFeedbackSentiment(SentimentResponse feedbackSentiment) { this.feedbackSentiment = feedbackSentiment; }
   

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
 
}