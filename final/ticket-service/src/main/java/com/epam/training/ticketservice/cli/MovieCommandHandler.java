package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class MovieCommandHandler {

    private final AccountCommandHandler accountCommandHandler;
    private final MovieService movieService;

    public MovieCommandHandler(AccountCommandHandler accountCommandHandler, MovieService movieService) {
        this.accountCommandHandler = accountCommandHandler;
        this.movieService = movieService;
    }

    @ShellMethod(value = "Add a movie to database", key = {"create movie", "cm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createMovie(final String name, final String genre, final int length) {
        this.movieService.createMovie(new Movie(name, genre, length));
        return String.format("Movie with name '%s' successfully created.", name);
    }

    @ShellMethod(value = "Update a movie in the database", key = {"update movie", "um"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateMovie(final String name, final String genre, final int length) {
        try {
            this.movieService.updateMovie(new Movie(name, genre, length));
            return String.format("Movie with name '%s' successfully updated.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a movie from the database", key = {"delete movie", "dm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteMovie(final String name) {
        try {
            this.movieService.deleteMovie(name);
            return String.format("Movie with name '%s' successfully deleted.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the movies", key = {"list movies", "lm"})
    public String listMovies() {
        List<Movie> movies = this.movieService.getAllMovies();
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            movies.forEach(movie -> stringBuilder.append(String.format("%s (%s, %s minutes)\n",
                    movie.getName(), movie.getGenre(), movie.getLength())));
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
