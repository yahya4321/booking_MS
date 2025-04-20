package com.CheritSolutions.Booking_Microservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import com.CheritSolutions.Booking_Microservice.dto.FeedbackResponse;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        // Instant to LocalDateTime converter
        mapper.addConverter(ctx -> 
            LocalDateTime.ofInstant(ctx.getSource(), ZoneId.systemDefault()), 
            Instant.class, LocalDateTime.class);

        // Feedback mapping
        mapper.typeMap(Feedback.class, FeedbackResponse.class)
            .addMappings(m -> m.map(src -> src.getBooking().getId(), FeedbackResponse::setBookingId));

        return mapper;
    }
}