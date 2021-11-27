package com.epam.training.ticketservice.service.exception;

public class SeatsTakenException extends RuntimeException {

    public SeatsTakenException(String message) {
        super(message);
    }
}
