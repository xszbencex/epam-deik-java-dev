package com.epam.training.ticketservice.service.exception;

public class NoSuchSeatException extends RuntimeException {

    public NoSuchSeatException(String message) {
        super(message);
    }
}
