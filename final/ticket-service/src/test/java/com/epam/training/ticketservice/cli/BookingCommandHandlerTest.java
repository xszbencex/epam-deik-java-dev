package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import com.epam.training.ticketservice.service.exception.SeatsTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class BookingCommandHandlerTest {

    private final static Account ADMIN = new Account("admin", "admin", true);
    private final static Account USER = new Account("user", "user");
    private final static String movieName = "Movie";
    private final static String roomName = "Room";
    private final static String startingAt = "2000-12-13 10:10";
    private final static String seats = "1,2 3,4";

    private BookingCommandHandler underTest;

    @Mock
    private AccountService accountService;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new BookingCommandHandler(bookingService, accountService);
    }


    @Test
    void testBookShouldReturnErrorMessageWhenNoSuchItemExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(bookingService).createBookingByIds(movieName, roomName, startingAt, seats, USER);
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final String actual = underTest.book(movieName, roomName, startingAt, seats);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testBookShouldReturnErrorMessageWhenSeatsTakenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new SeatsTakenException(expected)).when(bookingService).createBookingByIds(movieName, roomName, startingAt, seats, USER);
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final String actual = underTest.book(movieName, roomName, startingAt, seats);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testBookShouldReturnFormattedMessageWhenEverythingIsOk() {
        // Given
        final String expected = "Formatted message";
        doNothing().when(bookingService).createBookingByIds(movieName, roomName, startingAt, seats, USER);
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));
        given(bookingService.formattedBookingMessage(movieName, roomName, startingAt, seats)).willReturn(expected);

        // When
        final String actual = underTest.book(movieName, roomName, startingAt, seats);

        // Then
        verify(bookingService).createBookingByIds(movieName, roomName, startingAt, seats, USER);
        assertEquals(expected, actual);
    }

    @Test
    void testCheckLoggedInWithDefaultAccountAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command is only available for logged in users without admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkLoggedInWithDefaultAccountAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckLoggedInWithDefaultAccountAvailabilityShouldReturnUnavailableWhenAdminIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command is only available for logged in users without admin privileges.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(ADMIN));

        // When
        final Availability actual = underTest.checkLoggedInWithDefaultAccountAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckLoggedInWithDefaultAccountAvailabilityShouldReturnAvailableWhenDefaultUserIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkLoggedInWithDefaultAccountAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
