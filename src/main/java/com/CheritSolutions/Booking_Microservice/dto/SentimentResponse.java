package com.CheritSolutions.Booking_Microservice.dto;

public class SentimentResponse {
    private Long positiveComments;
    private Long negativeComments;
    private Double positivePercentage;

    public SentimentResponse() {}
    public SentimentResponse(Long positiveComments, Long negativeComments, Double positivePercentage) {
        this.positiveComments = positiveComments;
        this.negativeComments = negativeComments;
        this.positivePercentage = positivePercentage;
    }

    public Long getPositiveComments() { return positiveComments; }
    public void setPositiveComments(Long positiveComments) { this.positiveComments = positiveComments; }
    public Long getNegativeComments() { return negativeComments; }
    public void setNegativeComments(Long negativeComments) { this.negativeComments = negativeComments; }
    public Double getPositivePercentage() { return positivePercentage; }
    public void setPositivePercentage(Double positivePercentage) { this.positivePercentage = positivePercentage; }
}