package com.CheritSolutions.Booking_Microservice.services;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.BookingStatus;
import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import com.CheritSolutions.Booking_Microservice.Controllers.BookingController;
import com.CheritSolutions.Booking_Microservice.Controllers.BookingException;
import com.CheritSolutions.Booking_Microservice.client.BusinessServiceClient;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import com.CheritSolutions.Booking_Microservice.dto.BusinessResponse;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackEvent;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackRequest;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackResponse;
import com.CheritSolutions.Booking_Microservice.dto.ServiceResponse;
import com.CheritSolutions.Booking_Microservice.dto.StaffResponse;
import com.CheritSolutions.Booking_Microservice.repositories.BookingRepository;
import com.CheritSolutions.Booking_Microservice.repositories.FeedbackRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class FeedbackService {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class); // Declare logger

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

    @Autowired
    private KafkaTemplate<String, FeedbackEvent> kafkaTemplate;

    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request, UUID clientId) {
        try {

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
            // Fetch business details to get email and other details

            // Publish feedback event to Kafka
            FeedbackEvent event = new FeedbackEvent(
                    savedFeedback.getId(),
                    savedFeedback.getBooking().getId(),
                    booking.getServiceId(),
                    booking.getStaffId(),
                    savedFeedback.getServiceRating(),
                    savedFeedback.getStaffRating());
            kafkaTemplate.send("feedback.created", event.getFeedbackId().toString(), event);
            log.info("Published feedback event to Kafka: feedbackId={}", event.getFeedbackId());

            BusinessResponse business = businessServiceClient.getBusinessById(booking.getBusinessId());
            String businessEmail = business.getEmail(); // Assuming BusinessResponse has getEmail()
            if (businessEmail == null || businessEmail.isEmpty()) {
            } else {
                // Fetch service and staff details to create BookingResponse
                ServiceResponse service = businessServiceClient.getServiceByBusiness(
                        booking.getBusinessId(), booking.getServiceId());
                StaffResponse staff = businessServiceClient.getStaff(
                        booking.getBusinessId(), booking.getStaffId()).getBody();

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
                        booking.getStatus());

                // Send feedback notification email to business
                emailService.sendBusinessFeedbackEmail(
                        businessEmail,
                        business.getName(),
                        feedback,
                        bookingResponse);
            }

            return modelMapper.map(savedFeedback, FeedbackResponse.class);
        } catch (FeignException e) {
            throw new BookingException("Failed to submit feedback to business service", e);
        } catch (BookingException e) {
            throw e;
        } catch (Exception e) {
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