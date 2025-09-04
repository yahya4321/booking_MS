package com.CheritSolutions.Booking_Microservice.dto;

public class SentimentResponse {
    private Long positiveComments;
    private Long negativeComments;
    private Long totalFeedbackCount; // Total feedback count for clarity
    private Double positivePercentage;

    // Default constructor
    public SentimentResponse() {}

    // Constructor with wrapper types (existing)
    public SentimentResponse(Long positiveComments, Long negativeComments, Long totalFeedbackCount, Double positivePercentage) {
        this.positiveComments = positiveComments;
        this.negativeComments = negativeComments;
        this.totalFeedbackCount = totalFeedbackCount;
        this.positivePercentage = positivePercentage;
    }

    // New constructor with primitive types to fix the error
    public SentimentResponse(long positiveComments, long negativeComments, long totalFeedbackCount, double positivePercentage) {
        this.positiveComments = positiveComments;
        this.negativeComments = negativeComments;
        this.totalFeedbackCount = totalFeedbackCount;
        this.positivePercentage = positivePercentage;
    }

    // Getters and Setters
    public Long getPositiveComments() { return positiveComments; }
    public void setPositiveComments(Long positiveComments) { this.positiveComments = positiveComments; }
    public Long getNegativeComments() { return negativeComments; }
    public void setNegativeComments(Long negativeComments) { this.negativeComments = negativeComments; }
    public Long getTotalFeedbackCount() { return totalFeedbackCount; }
    public void setTotalFeedbackCount(Long totalFeedbackCount) { this.totalFeedbackCount = totalFeedbackCount; }
    public Double getPositivePercentage() { return positivePercentage; }
    public void setPositivePercentage(Double positivePercentage) { this.positivePercentage = positivePercentage; }
}