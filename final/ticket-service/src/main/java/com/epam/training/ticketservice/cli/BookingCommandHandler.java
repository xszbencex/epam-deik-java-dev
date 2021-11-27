package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.SeatsTakenException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class BookingCommandHandler {

    private final BookingService bookingService;
    private final AccountService accountService;

    public BookingCommandHandler(final BookingService bookingService,final  AccountService accountService) {
        this.bookingService = bookingService;
        this.accountService = accountService;
    }

    @ShellMethod(value = "Reserve tickets to seats on existing screening", key = {"book", "b"})
    @ShellMethodAvailability("checkLoggedInWithDefaultAccountAvailability")
    public String book(final String movieName, final String roomName, final String startingAt, final String seats) {
        try {
            this.bookingService.createBookingByIds(movieName, roomName, startingAt, seats,
                    accountService.getLoggedInAccount().get());
            return this.bookingService.formattedBookingMessage(movieName, roomName, startingAt, seats);
        } catch (NoSuchItemException | SeatsTakenException e) {
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
