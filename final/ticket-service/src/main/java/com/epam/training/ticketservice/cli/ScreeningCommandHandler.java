package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.ScreeningService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ShellComponent
public class ScreeningCommandHandler {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final AccountCommandHandler accountCommandHandler;
    private final ScreeningService screeningService;
    private final MovieService movieService;

    public ScreeningCommandHandler(AccountCommandHandler accountCommandHandler,
                                   ScreeningService screeningService,
                                   MovieService movieService) {
        this.accountCommandHandler = accountCommandHandler;
        this.screeningService = screeningService;
        this.movieService = movieService;
    }

    @ShellMethod(value = "Add a screening to database", key = {"create screening", "cs"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createScreening(final String movieName, final String roomName, final String startingAt) {
        try {
            this.screeningService.createScreening(
                    new Screening(movieName, roomName, LocalDateTime.parse(startingAt, dateTimeFormatter)));
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
            this.screeningService.deleteScreening(
                    new ScreeningId(movieName, roomName, LocalDateTime.parse(startingAt, dateTimeFormatter)));
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
            return "There are no screenings at the moment";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            screenings.forEach(screening -> {
                final Movie movie = this.movieService.getMovieById(screening.getMovieName()).orElseThrow();
                stringBuilder.append(String.format("%s (%s, %s minutes), screened in room %s, at %s\n",
                        movie.getName(),
                        movie.getGenre(),
                        movie.getLength(),
                        screening.getRoomName(),
                        screening.getStartingAt().format(dateTimeFormatter)));
            });
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
    }

    public Availability checkAdminAvailability() {
        return this.accountCommandHandler.getLoggedInAccount().filter(Account::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
