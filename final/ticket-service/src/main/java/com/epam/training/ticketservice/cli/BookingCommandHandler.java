package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class BookingCommandHandler {

    private final AccountCommandHandler accountCommandHandler;

    public BookingCommandHandler(AccountCommandHandler accountCommandHandler) {
        this.accountCommandHandler = accountCommandHandler;
    }

    @ShellMethod(value = "Reserve tickets to seats on existing screening", key = {"book", "b"})
    @ShellMethodAvailability("checkLoggedInWithDefaultAccountAvailability")
    public String book(String movieName, String roomName, String startingAt, String seats) {
        return "";
    }

    public Availability checkLoggedInWithDefaultAccountAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
