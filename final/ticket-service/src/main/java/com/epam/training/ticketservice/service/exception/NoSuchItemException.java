package com.epam.training.ticketservice.service.exception;

public class NoSuchItemException extends RuntimeException {

    public NoSuchItemException(String message) {
        super(message);
    }
}
