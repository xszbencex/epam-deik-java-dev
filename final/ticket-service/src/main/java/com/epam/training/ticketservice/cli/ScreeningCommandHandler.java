package com.epam.training.ticketservice.cli;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class ScreeningCommandHandler {

    private final AccountCommandHandler accountCommandHandler;

    public ScreeningCommandHandler(AccountCommandHandler accountCommandHandler) {
        this.accountCommandHandler = accountCommandHandler;
    }

    @ShellMethod(value = "Add a screening to database", key = {"create screening", "cs"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createScreening(final String name, final String rowNumber, final int columnNumber) {
        return "";
    }

    @ShellMethod(value = "Delete a screening from the database", key = {"delete screening", "ds"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteScreening(final String name) {
        return "";
    }

    @ShellMethod(value = "List the screenings", key = {"list screenings", "ls"})
    public String listScreenings() {
        if (false) {
            return "There are no screenings at the moment";
        } else {
            return "";
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().isPresent()
                && this.accountCommandHandler.getLoggedInAccount().get().getAdmin()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
