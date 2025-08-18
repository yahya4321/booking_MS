package com.CheritSolutions.Booking_Microservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class TrendResponse {
    private List<DailyTrendDTO> dailyTrends;

    public TrendResponse() {}
    public TrendResponse(List<DailyTrendDTO> dailyTrends) {
        this.dailyTrends = dailyTrends;
    }

    public List<DailyTrendDTO> getDailyTrends() { return dailyTrends; }
    public void setDailyTrends(List<DailyTrendDTO> dailyTrends) { this.dailyTrends = dailyTrends; }

    public static class DailyTrendDTO {
        private String bookingDate;
        private Long bookingCount;
        private BigDecimal totalRevenue;

        public DailyTrendDTO() {}
        public DailyTrendDTO(String bookingDate, Long bookingCount, BigDecimal totalRevenue) {
            this.bookingDate = bookingDate;
            this.bookingCount = bookingCount;
            this.totalRevenue = totalRevenue;
        }

        public String getBookingDate() { return bookingDate; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
        public Long getBookingCount() { return bookingCount; }
        public void setBookingCount(Long bookingCount) { this.bookingCount = bookingCount; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }
}