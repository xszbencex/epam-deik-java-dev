package com.epam.training.ticketservice.cli;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class MovieCommandHandler {

    @ShellMethod(value = "Add a movie to database", key = "create movie")
    public String createMovie(final String name, final String genre, final int length) {
        return "";
    }

    @ShellMethod(value = "Update a movie in the database", key = "update movie")
    public String updateMovie(final String name, final String genre, final int length) {
        return "";
    }

    @ShellMethod(value = "Delete a movie from the database", key = "delete movie")
    public String deleteMovie(final String name) {
        return "";
    }

    @ShellMethod(value = "List the movies", key = "list movies")
    public String listMovies() {
        if (false) {
            return "There are no movies at the moment";
        } else {
            return "";
        }
    }
}
