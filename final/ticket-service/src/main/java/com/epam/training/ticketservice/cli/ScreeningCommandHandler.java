package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
public class ScreeningCommandHandler {

    @Value("${ticket-service.date-time.pattern}")
    private String dateTimePattern;

    private final ScreeningService screeningService;
    private final AccountService accountService;

    public ScreeningCommandHandler(ScreeningService screeningService, AccountService accountService) {
        this.screeningService = screeningService;
        this.accountService = accountService;
    }

    @ShellMethod(value = "Add a screening to database", key = {"create screening", "cs"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createScreening(final String movieName, final String roomName, final String startingAt) {
        try {
            this.screeningService.createScreeningFromIds(
                    movieName,
                    roomName,
                    LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern(dateTimePattern)));
            return String.format("Screening to '%s' in %s at %s successfully created.",
                    movieName, roomName, startingAt);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a screening from the database", key = {"delete screening", "ds"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteScreening(final String movieName, final String roomName, final String startingAt) {
        try {
            this.screeningService.deleteScreening(this.screeningService.constructScreeningIdFromIds(
                    movieName,
                    roomName,
                    LocalDateTime.parse(startingAt, DateTimeFormatter.ofPattern(dateTimePattern))));
            return String.format("Screening to '%s' in %s at %s successfully deleted.",
                    movieName, roomName, startingAt);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the screenings", key = {"list screenings", "ls"})
    public String listScreenings() {
        List<Screening> screenings = this.screeningService.getAllScreenings();
        if (screenings.isEmpty()) {
            return "There are no screenings";
        } else {
            return screeningService.formattedScreeningList(screenings);
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountService.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
