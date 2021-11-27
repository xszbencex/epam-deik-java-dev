package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.service.AccountService;
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

    private final BookingService bookingService;
    private final AccountService accountService;

    public BookingCommandHandler(BookingService bookingService, AccountService accountService) {
        this.bookingService = bookingService;
        this.accountService = accountService;
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
                    accountService.getLoggedInAccount().get());
            return this.bookingService.formattedBookingMessage(seats);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Availability checkLoggedInWithDefaultAccountAvailability() {
        return this.accountService.getLoggedInAccount().filter(account -> !account.getAdmin()).isPresent()
                ? Availability.available()
                : Availability.unavailable(
                        "this command is only available for logged in users without admin privileges.");
    }
}
