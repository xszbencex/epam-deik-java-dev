package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Movie;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.model.config.ScreeningId;
import com.epam.training.ticketservice.model.config.Seat;
import com.epam.training.ticketservice.repositry.AccountRepository;
import com.epam.training.ticketservice.service.exception.UsernameTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class AccountServiceTest {

    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    private final static Account admin = new Account("admin", "admin", true);
    private final static Account user = new Account("user", "user", false);
    private final static Room room1 = new Room("Room1", 10, 10);
    private final static Room room2 = new Room("Room2", 10, 10);
    private final static Movie movie1 = new Movie("Movie1", "action", 100);
    private final static Movie movie2 = new Movie("Movie2", "animation", 110);
    private final static Screening screening1 = new Screening(new ScreeningId(movie1, room1,
            LocalDateTime.parse("2000-12-13 10:10", DateTimeFormatter.ofPattern(dateTimePattern))));
    private final static Screening screening2 = new Screening(new ScreeningId(movie2, room2,
            LocalDateTime.parse("2000-12-15 15:00", DateTimeFormatter.ofPattern(dateTimePattern))));
    private final static Booking booking1 = new Booking(screening1, List.of(new Seat("5,5"), new Seat("5,6")), user, 3000L);
    private final static Booking booking2 = new Booking(screening2, List.of(new Seat("1,2")), user, 1500L);

    private AccountService underTest;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new AccountService(accountRepository, bookingService);
    }

    @Test
    void testSignInShouldSetLoggedInAccountToGivenAccount() {
        // Given + When
        underTest.signIn(admin);

        // Then
        assertEquals(Optional.of(admin), underTest.getLoggedInAccount());
    }

    @Test
    void testSignOutShouldSetLoggedInAccountToOptionalEmpty() {
        // Given + When
        underTest.signOut();

        // Then
        assertEquals(Optional.empty(), underTest.getLoggedInAccount());
    }

    @Test
    void testCreateAccountShouldThrowExceptionWhenUsernameIsAlreadyTaken() {
        // Given
        given(accountRepository.findById(admin.getUsername())).willReturn(Optional.of(admin));

        // When + Then
        assertThrows(UsernameTakenException.class, () -> underTest.createAccount(admin));
    }

    @Test
    void testCreateAccountShouldCallRepositorySaveWhenUsernameIsRight() {
        // Given
        given(accountRepository.findById(admin.getUsername())).willReturn(Optional.empty());

        // When
        underTest.createAccount(admin);

        // Then
        verify(accountRepository).save(admin);
    }

    @Test
    void testGetAccountByIdShouldCallFindById() {
        // Given + When
        underTest.getAccountById(admin.getUsername());

        // Then
        verify(accountRepository).findById(admin.getUsername());
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testFormattedAccountDescriptionShouldReturnExpectedResultWhenIsAdmin() {
        // Given
        final String expected = "Signed in with privileged account 'admin'";

        // When
        final String actual = underTest.formattedAccountDescription(admin);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testFormattedAccountDescriptionShouldReturnExpectedResultWhenUserAndHasNoBookings() {
        // Given
        final String expected = "Signed in with account 'user'\nYou have not booked any tickets yet";
        final Account account = user;
        given(bookingService.getBookingsByUsername(account.getUsername())).willReturn(List.of());

        // When
        final String actual = underTest.formattedAccountDescription(account);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testFormattedAccountDescriptionShouldReturnExpectedResultWhenIsAdminAndHasBookings() {
        // Given
        final String expected = "Signed in with account 'user'\n" +
                "Your previous bookings are\n" +
                "Seats (5,5), (5,6) on Movie1 in room Room1 starting at 2000-12-13 10:10 for 3000 HUF\n" +
                "Seats (1,2) on Movie2 in room Room2 starting at 2000-12-15 15:00 for 1500 HUF";
        final Account account = user;
        given(bookingService.getBookingsByUsername(account.getUsername())).willReturn(List.of(booking1, booking2));

        // When
        final String actual = underTest.formattedAccountDescription(account);

        // Then
        assertEquals(expected, actual);
    }
}
