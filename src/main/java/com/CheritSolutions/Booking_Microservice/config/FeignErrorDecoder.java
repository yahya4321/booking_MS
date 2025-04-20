package com.CheritSolutions.Booking_Microservice.config;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

import javax.management.ServiceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new FeignException.NotFound("Resource not found", response.request(), null, null);
        }
        return new Default().decode(methodKey, response);
    }
}