package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.model.Account;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.exception.UsernameTakenException;
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

class AccountCommandHandlerTest {

    private final static Account ADMIN = new Account("admin", "admin", true);
    private final static Account USER = new Account("user", "user");

    private AccountCommandHandler underTest;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new AccountCommandHandler(accountService);
    }

    @Test
    void testSignUpShouldReturnErrorMessageWhenUsernameIsTaken() {
        // Given
        final String expected = "The given username is already taken!";
        doThrow(new UsernameTakenException()).when(accountService).createAccount(any(Account.class));

        // When
        final String actual = underTest.signUp(USER.getUsername(), USER.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignUpShouldReturnSuccessMessageWhenUsernameCreated() {
        // Given
        final String expected = "Successfully signed up as 'user'";
        doNothing().when(accountService).createAccount(USER);

        // When
        final String actual = underTest.signUp(USER.getUsername(), USER.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInShouldReturnIncorrectCredentialsWhenUsernameIsNotFound() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(USER.getUsername())).willReturn(Optional.empty());

        // When
        final String actual = underTest.signIn(USER.getUsername(), USER.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInShouldReturnIncorrectCredentialsWhenPasswordIsNotCorrect() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(USER.getUsername())).willReturn(Optional.of(USER));

        // When
        final String actual = underTest.signIn(USER.getUsername(), "wrong-password");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInShouldReturnPrivilegedErrorWhenAdminTriesToSignIn() {
        // Given
        final String expected = "'admin' is a privileged user. Privileged users must log in with the 'sign up privileged' command.";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.of(ADMIN));

        // When
        final String actual = underTest.signIn(ADMIN.getUsername(), ADMIN.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInShouldReturnSuccessMessageWhenEverythingIsFine() {
        // Given
        final String expected = "Successfully signed in";
        given(accountService.getAccountById(USER.getUsername())).willReturn(Optional.of(USER));
        doNothing().when(accountService).signIn(USER);

        // When
        final String actual = underTest.signIn(USER.getUsername(), USER.getPassword());

        // Then
        verify(accountService).signIn(USER);
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnIncorrectCredentialsWhenUsernameIsNotFound() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.empty());

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), ADMIN.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnIncorrectCredentialsWhenPasswordIsNotCorrect() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.of(ADMIN));

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), "wrong-password");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnPrivilegedErrorWhenDefaultUserTriesToSignIn() {
        // Given
        final String expected = "'user' is not a privileged user";
        given(accountService.getAccountById(USER.getUsername())).willReturn(Optional.of(USER));

        // When
        final String actual = underTest.signInPrivileged(USER.getUsername(), USER.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnSuccessMessageWhenEverythingIsFine() {
        // Given
        final String expected = "Successfully signed in";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.of(ADMIN));
        doNothing().when(accountService).signIn(ADMIN);

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), ADMIN.getPassword());

        // Then
        verify(accountService).signIn(ADMIN);
        assertEquals(expected, actual);
    }

    @Test
    void testSignOutShouldReturnSuccessMessageAndCallService() {
        // Given
        final String expected = "Successfully signed out";
        doNothing().when(accountService).signOut();

        // When
        final String actual = underTest.signOut();

        // Then
        verify(accountService).signOut();
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeAccountShouldReturnErrorMessageWhenNotSignedIn() {
        // Given
        final String expected = "You are not signed in";
        given(accountService.getLoggedInAccount()).willReturn(Optional.empty());

        // When
        final String actual = underTest.describeAccount();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeAccountShouldReturnFormattedMessageWhenSignedIn() {
        // Given
        final String expected = "Formatted message";
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));
        given(accountService.formattedAccountDescription(USER)).willReturn(expected);

        // When
        final String actual = underTest.describeAccount();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCheckLoggedInAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("you are not signed in.");
        given(accountService.getLoggedInAccount()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkLoggedInAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckLoggedInAvailabilityShouldReturnAvailableWhenSomeoneIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInAccount()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkLoggedInAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
