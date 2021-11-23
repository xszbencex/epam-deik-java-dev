package com.epam.training.ticketservice.service.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException() {
        super("The given username is already taken!");
    }
}
