package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.PriceComponent;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.PriceService;
import com.epam.training.ticketservice.service.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class PriceCommandHandlerTest {

    private final static int basePrice = 1500;

    private final static Account ADMIN = new Account("admin", "admin", true);
    private final static Account USER = new Account("user", "user");
    private final static String movieName = "Movie";
    private final static String roomName = "Room";
    private final static String startingAt = "2000-12-13 10:10";
    private final static String seats = "1,2 3,4";
    private final static int newBasePrice = 2000;
    private final static String priceComponentName = "discount";
    private final static int priceComponentAmount = -200;

    private PriceCommandHandler underTest;

    @Mock
    private AccountService accountService;

    @Mock
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PriceCommandHandler(priceService, accountService);
    }

    @Test
    void testUpdateBasePriceShouldCallService() {
        // Given + When
        underTest.updateBasePrice(newBasePrice);

        // Then
        verify(priceService).setBasePrice(newBasePrice);
    }

    @Test
    void testCreatePriceComponentShouldCallServiceAndReturnSuccessMessage() {
        // Given
        final String expected = "Price component with name 'discount' successfully created.";
        doNothing().when(priceService).createPriceComponent(any(PriceComponent.class));

        // When
        final String actual = underTest.createPriceComponent(priceComponentName, priceComponentAmount);

        // Then
        verify(priceService).createPriceComponent(any(PriceComponent.class));
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToRoomShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(priceService)
                .attachPriceComponentToRoom(priceComponentName, roomName);

        // When
        final String actual = underTest.attachPriceComponentToRoom(priceComponentName, roomName);

        // Then
        verify(priceService).attachPriceComponentToRoom(priceComponentName, roomName);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToRoomShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Price component with name 'discount' successfully attached to room 'Room'.";
        doNothing().when(priceService).attachPriceComponentToRoom(priceComponentName, roomName);

        // When
        final String actual = underTest.attachPriceComponentToRoom(priceComponentName, roomName);

        // Then
        verify(priceService).attachPriceComponentToRoom(priceComponentName, roomName);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToMovieShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(priceService)
                .attachPriceComponentToMovie(priceComponentName, movieName);

        // When
        final String actual = underTest.attachPriceComponentToMovie(priceComponentName, movieName);

        // Then
        verify(priceService).attachPriceComponentToMovie(priceComponentName, movieName);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToMovieShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Price component with name 'discount' successfully attached to movie 'Movie'.";
        doNothing().when(priceService).attachPriceComponentToMovie(priceComponentName, movieName);

        // When
        final String actual = underTest.attachPriceComponentToMovie(priceComponentName, movieName);

        // Then
        verify(priceService).attachPriceComponentToMovie(priceComponentName, movieName);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToScreeningShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(priceService)
                .attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);

        // When
        final String actual = underTest.attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);

        // Then
        verify(priceService).attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToScreeningShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Price component with name 'discount' successfully attached to the screening.";
        doNothing().when(priceService).attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);

        // When
        final String actual = underTest.attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);

        // Then
        verify(priceService).attachPriceComponentToScreening(priceComponentName, movieName, roomName, startingAt);
        assertEquals(expected, actual);
    }

    @Test
    void testShowPriceOfBookingShouldReturnExpectedString() {
        // Given
        final int numberOfSeats = 2;
        final String expected = "The price for this booking would be 3000 HUF";
        given(priceService.calculatePriceForBooking(movieName, roomName, startingAt, numberOfSeats))
                .willReturn((long) basePrice * numberOfSeats);

        // When
        final String actual = underTest.showPriceOfBooking(movieName, roomName, startingAt, seats);

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
