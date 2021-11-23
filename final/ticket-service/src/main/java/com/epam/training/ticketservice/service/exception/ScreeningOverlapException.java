package com.epam.training.ticketservice.service.exception;

public class ScreeningOverlapException extends RuntimeException {

    public ScreeningOverlapException(String message) {
        super(message);
    }
}
