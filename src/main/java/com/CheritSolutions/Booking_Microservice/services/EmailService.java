package com.CheritSolutions.Booking_Microservice.services;

import com.CheritSolutions.Booking_Microservice.Entities.Booking;
import com.CheritSolutions.Booking_Microservice.Entities.Feedback;
import com.CheritSolutions.Booking_Microservice.dto.BookingResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a")
                        .withLocale(Locale.US)
                        .withZone(ZoneId.of("UTC"));


                        private static final DateTimeFormatter DATE_TIME_FORMATTER_TUNIS = 
                        DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a")
                                        .withLocale(Locale.US)
                                        .withZone(ZoneId.of("Africa/Tunis"));
    @Async
    public void sendBookingConfirmationEmail(String recipientEmail,String name, BookingResponse booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipientEmail);
            helper.setSubject("Booking Confirmation - " + booking.getBusinessName());
            helper.setFrom("yahyaaahamdi8756@gmail.com"); // Replace with your sender email


            // Format the slotStart time
            String formattedDateTime = DATE_TIME_FORMATTER.format(booking.getSlotStart());


            String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                            overflow: hidden;
                        }
                        .header {
                            background-color: #4CAF50;
                            color: #ffffff;
                            padding: 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .content p {
                            line-height: 1.6;
                            margin: 10px 0;
                        }
                        .details {
                            background-color: #f9f9f9;
                            padding: 15px;
                            border-radius: 5px;
                            margin-top: 20px;
                        }
                        .details h3 {
                            margin-top: 0;
                            color: #4CAF50;
                            font-size: 18px;
                        }
                        .details ul {
                            list-style: none;
                            padding: 0;
                        }
                        .details ul li {
                            margin: 8px 0;
                            font-size: 14px;
                        }
                        .details ul li strong {
                            display: inline-block;
                            width: 140px;
                        }
                        .footer {
                            background-color: #4CAF50;
                            color: #ffffff;
                            text-align: center;
                            padding: 10px;
                            font-size: 12px;
                        }
                        .footer a {
                            color: #ffffff;
                            text-decoration: underline;
                        }
                        @media only screen and (max-width: 600px) {
                            .container {
                                margin: 10px;
                            }
                            .header h1 {
                                font-size: 20px;
                            }
                            .details ul li strong {
                                width: 100px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Booking Confirmation</h1>
                        </div>
                        <div class="content">
                            <p>Dear <strong>%s</strong></p>
                            <p>Thank you for your booking with <strong>%s</strong>. We're excited to confirm your appointment. Below are the details:</p>
                            <div class="details">
                                <h3>Booking Details</h3>
                                <ul>
                                    <li><strong>Business:</strong> %s</li>
                                    <li><strong>Address:</strong> %s</li>
                                    <li><strong>Service:</strong> %s</li>
                                    <li><strong>Price:</strong> %s DT</li>
                                    <li><strong>Staff Member:</strong> %s (%s)</li>
                                    <li><strong>Date & Time:</strong> %s</li>
                                    <li><strong>Duration:</strong> %d minutes</li>
                                    <li><strong>Status:</strong> %s</li>
                                </ul>
                            </div>
                        </div>
                        <div class="footer">
                            <p>Cherit Solutions &copy; 2025 | <a href="https://cheritsolutions.com">Visit our website</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                    name,
                    booking.getBusinessName(),
                    booking.getBusinessName(),
                    booking.getBusinessAddress(),
                    booking.getServiceName(),
                    booking.getServicePrice(),
                    booking.getStaffName(),
                    booking.getStaffPosition(),
                    formattedDateTime,
                    booking.getDuration(),
                    booking.getStatus()
                );

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Sent confirmation email to {} for booking ID: {}", recipientEmail, booking.getId());
        } catch (MessagingException e) {
            log.error("Failed to send email to {} for booking ID: {}. Error: {}", 
                recipientEmail, booking.getId(), e.getMessage());
        }
    }


    @Async
    public void sendBusinessFeedbackEmail(String businessEmail, String businessName, Feedback feedback, BookingResponse booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(businessEmail);
            helper.setSubject("New Feedback Received for Booking");
            helper.setFrom("yahyaaahamdi8756@gmail.com"); // Replace with your sender email
    
            String formattedDateTime = DATE_TIME_FORMATTER_TUNIS.format(feedback.getCreatedAt());
            String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                            overflow: hidden;
                        }
                        .header {
                            background-color: #2196F3;
                            color: #ffffff;
                            padding: 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .content p {
                            line-height: 1.6;
                            margin: 10px 0;
                        }
                        .details {
                            background-color: #f9f9f9;
                            padding: 15px;
                            border-radius: 5px;
                            margin-top: 20px;
                        }
                        .details h3 {
                            margin-top: 0;
                            color: #2196F3;
                            font-size: 18px;
                        }
                        .details ul {
                            list-style: none;
                            padding: 0;
                        }
                        .details ul li {
                            margin: 8px 0;
                            font-size: 14px;
                        }
                        .details ul li strong {
                            display: inline-block;
                            width: 140px;
                        }
                        .footer {
                            background-color: #2196F3;
                            color: #ffffff;
                            text-align: center;
                            padding: 10px;
                            font-size: 12px;
                        }
                        .footer a {
                            color: #ffffff;
                            text-decoration: underline;
                        }
                        @media only screen and (max-width: 600px) {
                            .container {
                                margin: 10px;
                            }
                            .header h1 {
                                font-size: 20px;
                            }
                            .details ul li strong {
                                width: 100px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>New Feedback Received</h1>
                        </div>
                        <div class="content">
                            <p>Dear <strong>%s</strong>,</p>
                            <p>A new feedback has been submitted for a recent booking. Below are the details:</p>
                            <div class="details">
                                <h3>Feedback Details</h3>
                                <ul>
                                    <li><strong>Service:</strong> %s</li>
                                    <li><strong>Staff Member:</strong> %s (%s)</li>
                                    <li><strong>Service Rating:</strong> %d/5</li>
                                    <li><strong>Staff Rating:</strong> %d/5</li>
                                    <li><strong>Service Comment:</strong> %s</li>
                                    <li><strong>Staff Comment:</strong> %s</li>
                                    <li><strong>Anonymous:</strong> %s</li>
                                    <li><strong>Submitted At:</strong> %s</li>
                                </ul>
                            </div>
                        </div>
                        <div class="footer">
                            <p>Cherit Solutions © 2025 | <a href="https://cheritsolutions.com">Visit our website</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                    businessName,
                    booking.getServiceName() != null ? booking.getServiceName() : "Unknown",
                    booking.getStaffName() != null ? booking.getStaffName() : "Unknown",
                    booking.getStaffPosition() != null ? booking.getStaffPosition() : "Unknown",
                    feedback.getServiceRating(),
                    feedback.getStaffRating(),
                    feedback.getServiceComment() != null ? feedback.getServiceComment() : "No comment",
                    feedback.getStaffComment() != null ? feedback.getStaffComment() : "No comment",
                    feedback.getIsAnonymous() ? "Yes" : "No",
                    formattedDateTime                );

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Sent feedback notification email to {} for booking ID: {}", businessEmail, booking.getId());
        } catch (MessagingException e) {
            log.error("Failed to send feedback email to {} for booking ID: {}. Error: {}", 
                businessEmail, booking.getId(), e.getMessage());
        }
    }


    @Async
    public void sendBusinessBookingConfirmationEmail(String businessEmail, String businessName, BookingResponse booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(businessEmail);
            helper.setSubject("New Booking Confirmation - " + booking.getBusinessName());
            helper.setFrom("yahyaaahamdi8756@gmail.com"); // Replace with your sender email

            // Format the slotStart time
            String formattedDateTime = DATE_TIME_FORMATTER.format(booking.getSlotStart());

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                            overflow: hidden;
                        }
                        .header {
                            background-color: #4CAF50;
                            color: #ffffff;
                            padding: 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .content p {
                            line-height: 1.6;
                            margin: 10px 0;
                        }
                        .details {
                            background-color: #f9f9f9;
                            padding: 15px;
                            border-radius: 5px;
                            margin-top: 20px;
                        }
                        .details h3 {
                            margin-top: 0;
                            color: #4CAF50;
                            font-size: 18px;
                        }
                        .details ul {
                            list-style: none;
                            padding: 0;
                        }
                        .details ul li {
                            margin: 8px 0;
                            font-size: 14px;
                        }
                        .details ul li strong {
                            display: inline-block;
                            width: 140px;
                        }
                        .footer {
                            background-color: #4CAF50;
                            color: #ffffff;
                            text-align: center;
                            padding: 10px;
                            font-size: 12px;
                        }
                        .footer a {
                            color: #ffffff;
                            text-decoration: underline;
                        }
                        @media only screen and (max-width: 600px) {
                            .container {
                                margin: 10px;
                            }
                            .header h1 {
                                font-size: 20px;
                            }
                            .details ul li strong {
                                width: 100px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>New Booking Confirmation</h1>
                        </div>
                        <div class="content">
                            <p>Dear <strong>%s</strong>,</p>
                            <p>A new booking has been made with your business, <strong>%s</strong>. Below are the details of the appointment:</p>
                            <div class="details">
                                <h3>Booking Details</h3>
                                <ul>
                                    <li><strong>Business:</strong> %s</li>
                                    <li><strong>Address:</strong> %s</li>
                                    <li><strong>Service:</strong> %s</li>
                                    <li><strong>Price:</strong> %s DT</li>
                                    <li><strong>Staff Member:</strong> %s (%s)</li>
                                    <li><strong>Date & Time:</strong> %s</li>
                                    <li><strong>Duration:</strong> %d minutes</li>
                                    <li><strong>Status:</strong> %s</li>
                                </ul>
                            </div>
                        </div>
                        <div class="footer">
                            <p>Cherit Solutions © 2025 | <a href="https://cheritsolutions.com">Visit our website</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                    businessName,
                    booking.getBusinessName(),
                    booking.getBusinessName(),
                    booking.getBusinessAddress(),
                    booking.getServiceName(),
                    booking.getServicePrice(),
                    booking.getStaffName(),
                    booking.getStaffPosition(),
                    formattedDateTime,
                    booking.getDuration(),
                    booking.getStatus()
                );

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Sent business confirmation email to {} for booking ID: {}", businessEmail, booking.getId());
        } catch (MessagingException e) {
            log.error("Failed to send business confirmation email to {} for booking ID: {}. Error: {}", 
                businessEmail, booking.getId(), e.getMessage());
        }
    }




}