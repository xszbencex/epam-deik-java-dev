package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.BookingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ShellComponent
public class BookingCommandHandler {

    @Value("${ticket-service.date-time.pattern}")
    private String dateTimePattern;

    private final AccountCommandHandler accountCommandHandler;
    private final BookingService bookingService;

    public BookingCommandHandler(AccountCommandHandler accountCommandHandler, BookingService bookingService) {
        this.accountCommandHandler = accountCommandHandler;
        this.bookingService = bookingService;
    }

    @ShellMethod(value = "Reserve tickets to seats on existing screening", key = {"book", "b"})
    @ShellMethodAvailability("checkLoggedInWithDefaultAccountAvailability")
    public String book(String movieName, String roomName, String startingAt, String seats) {
        try {
            this.bookingService.createBookingByIds(
                    movieName,
                    roomName,
                    LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern(dateTimePattern)),
                    seats,
                    accountCommandHandler.getLoggedInAccount().get());
            return this.bookingService.formattedBookingMessage(seats);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Availability checkLoggedInWithDefaultAccountAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().filter(account -> !account.getAdmin()).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command is only available for logged in users without admin privileges.");
    }
}
