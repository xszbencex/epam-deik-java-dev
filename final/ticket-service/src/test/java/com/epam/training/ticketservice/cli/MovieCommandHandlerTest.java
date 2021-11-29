package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.MovieService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class MovieCommandHandlerTest {

    private final static Account ADMIN = new Account("admin", "admin", true);
    private final static Account USER = new Account("user", "user");
    private final static String movieName = "Movie";
    private final static String movieGenre = "action";
    private final static Integer movieLength = 100;
    private final static Movie movie = new Movie(movieName, movieGenre, movieLength);

    private MovieCommandHandler underTest;

    @Mock
    private AccountService accountService;

    @Mock
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new MovieCommandHandler(movieService, accountService);
    }

    @Test
    void testCreateMovieShouldCallServiceAndReturnSuccessMessage() {
        // Given
        final String expected = "Movie with name 'Movie' successfully created.";
        doNothing().when(movieService).createMovie(any(Movie.class));

        // When
        final String actual = underTest.createMovie(movieName, movieGenre, movieLength);

        // Then
        verify(movieService).createMovie(any(Movie.class));
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateMovieShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(movieService).updateMovie(any(Movie.class));

        // When
        final String actual = underTest.updateMovie(movieName, movieGenre, movieLength);

        // Then
        verify(movieService).updateMovie(any(Movie.class));
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateMovieShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Movie with name 'Movie' successfully updated.";
        doNothing().when(movieService).updateMovie(any(Movie.class));

        // When
        final String actual = underTest.updateMovie(movieName, movieGenre, movieLength);

        // Then
        verify(movieService).updateMovie(any(Movie.class));
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteMovieShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(movieService).deleteMovie(movieName);

        // When
        final String actual = underTest.deleteMovie(movieName);

        // Then
        verify(movieService).deleteMovie(movieName);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteMovieShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Movie with name 'Movie' successfully deleted.";
        doNothing().when(movieService).deleteMovie(movieName);

        // When
        final String actual = underTest.deleteMovie(movieName);

        // Then
        verify(movieService).deleteMovie(movieName);
        assertEquals(expected, actual);
    }

    @Test
    void testListMoviesShouldReturnStringWhenNoMovieFound() {
        // Given
        final String expected = "There are no movies at the moment";
        given(movieService.getAllMovies()).willReturn(List.of());

        // When
        final String actual = underTest.listMovies();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testListMoviesShouldReturnFormattedStringWhenMoviesFound() {
        // Given
        final String expected = "Formatted message";
        given(movieService.getAllMovies()).willReturn(List.of(movie));
        given(movieService.formattedMovieList(anyList())).willReturn(expected);

        // When
        final String actual = underTest.listMovies();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }


    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenDefaultUserIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnAvailableWhenAdminIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(ADMIN));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
