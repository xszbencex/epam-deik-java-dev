package com.epam.training.ticketservice.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BookingCommandHandler {

    @ShellMethod(value = "Reserve tickets to seats on existing screening", key = "book")
    public String book(String movieName, String roomName, String startingAt, String seats) {
        return "";
    }
}
