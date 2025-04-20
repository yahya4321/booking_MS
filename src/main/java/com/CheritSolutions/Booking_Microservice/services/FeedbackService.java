package com.CheritSolutions.Booking_Microservice.services;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;
import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import com.CheritSolutions.Booking_Microservice.Controllers.BookingException;
import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackRequest;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;
import com.CheritSolutions.Booking_Microservice.repositories.BookingRepository;
import com.CheritSolutions.Booking_Microservice.repositories.FeedbackRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusinessServiceClient businessServiceClient;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request, UUID clientId) {
        try {
            log.info("Submitting feedback for booking: {}", request.getBookingId());

            // Fetch and validate booking
            Booking booking = bookingRepository.findByIdAndClientId(request.getBookingId(), clientId)
                .orElseThrow(() -> new BookingException("Booking not found or not owned by client"));

            // Check booking status
            if (booking.getStatus() != BookingStatus.CONFIRMED) {
                throw new BookingException("Feedback can only be submitted for confirmed bookings");
            }

            // Check if feedback already exists
            if (feedbackRepository.findByBookingId(request.getBookingId()).isPresent()) {
                throw new BookingException("Feedback already submitted for this booking");
            }

            // Create feedback entity
            Feedback feedback = Feedback.builder()
                .booking(booking)
                .clientId(clientId)
                .serviceRating(request.getServiceRating())
                .staffRating(request.getStaffRating())
                .serviceComment(request.getServiceComment())
                .staffComment(request.getStaffComment())
                .isAnonymous(request.getIsAnonymous())
                .createdAt(Instant.now())
                .build();

            // Save feedback
            Feedback savedFeedback = feedbackRepository.save(feedback);
            log.debug("Feedback saved: {}", savedFeedback.getId());

            // Fetch business details to get email and other details
            BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());
            String businessEmail = business.getEmail(); // Assuming BusinessResponse has getEmail()
            if (businessEmail == null || businessEmail.isEmpty()) {
                log.warn("No email found for business ID: {}", booking.getBusinessId());
            } else {
                // Fetch service and staff details to create BookingResponse
                ServiceResponse service = businessServiceClient.getServiceByBusiness(
                    booking.getBusinessId(), booking.getServiceId()
                );
                StaffResponse staff = businessServiceClient.getStaff(
                    booking.getBusinessId(), booking.getStaffId()
                ).getBody();

                // Create BookingResponse with enriched details
                BookingResponse bookingResponse = new BookingResponse(
                    booking.getId(),
                    booking.getClientId(),
                    booking.getServiceId(),
                    service.getName(),
                    service.getBasePrice(),
                    booking.getStaffId(),
                    staff != null ? staff.getName() : "Unknown",
                    staff != null ? staff.getPosition() : "Unknown",
                    booking.getBusinessId(),
                    business.getName(),
                    business.getAddress(),
                    booking.getSlotStart(),
                    booking.getDuration(),
                    booking.getSlotId(),
                    booking.getStatus()
                );

                // Send feedback notification email to business
                emailService.sendBusinessFeedbackEmail(
                    businessEmail,
                    business.getName(),
                    feedback,
                    bookingResponse
                );
                log.debug("Triggered feedback notification email to {} for booking ID: {}", businessEmail, booking.getId());
            }

            return modelMapper.map(savedFeedback, FeedbackResponse.class);
        } catch (FeignException e) {
            log.error("Feign error submitting feedback: {}", e.getMessage());
            throw new BookingException("Failed to submit feedback to business service", e);
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error submitting feedback", e);
            throw new BookingException("Failed to submit feedback: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackByBookingId(UUID bookingId, UUID clientId) {
        Feedback feedback = feedbackRepository.findByBookingIdAndClientId(bookingId, clientId)
            .orElseThrow(() -> new BookingException("Feedback not found for booking"));
        return modelMapper.map(feedback, FeedbackResponse.class);
    }
}