package com.epam.training.ticketservice.cli;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class MovieCommandHandler {

    private final AccountCommandHandler accountCommandHandler;

    public MovieCommandHandler(AccountCommandHandler accountCommandHandler) {
        this.accountCommandHandler = accountCommandHandler;
    }

    @ShellMethod(value = "Add a movie to database", key = {"create movie", "cm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createMovie(final String name, final String genre, final int length) {
        return "";
    }

    @ShellMethod(value = "Update a movie in the database", key = {"update movie", "um"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateMovie(final String name, final String genre, final int length) {
        return "";
    }

    @ShellMethod(value = "Delete a movie from the database", key = {"delete movie", "dm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteMovie(final String name) {
        return "";
    }

    @ShellMethod(value = "List the movies", key = {"list movies", "lm"})
    public String listMovies() {
        if (false) {
            return "There are no movies at the moment";
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
