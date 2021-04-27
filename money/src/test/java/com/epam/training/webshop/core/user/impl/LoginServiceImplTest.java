package com.epam.training.webshop.core.user.impl;

import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class LoginServiceImplTest {

    private LoginServiceImpl underTest;

    private UserService userService;

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserService.class);
        underTest = new LoginServiceImpl(userService);
    }

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.login(null, "pass"));

        // Then
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.login("user", null));

        // Then
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        // Given
        UserDto expectedUserDto = new UserDto("user", User.Role.USER);
        Optional<UserDto> expected = Optional.of(expectedUserDto);
        Mockito.when(userService.retrieveUserByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(expectedUserDto));

        // When
        Optional<UserDto> actual = underTest.login("user", "pass");

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, expectedUserDto), underTest);
        Mockito.verify(userService).retrieveUserByUsernameAndPassword("user", "pass");
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLoginShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        Mockito.when(userService.retrieveUserByUsernameAndPassword("user", "pass")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTest.login("user", "pass");

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, null), underTest);
        Mockito.verify(userService).retrieveUserByUsernameAndPassword("user", "pass");
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLogoutShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, null), underTest);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testLogoutShouldReturnThePreviouslyLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        UserDto expectedUserDto = new UserDto("user", User.Role.USER);
        underTest = new LoginServiceImpl(userService, expectedUserDto);
        Optional<UserDto> expected = Optional.of(expectedUserDto);

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, null), underTest);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetLoggedInUserShouldReturnTheLoggedInUserWhenThereIsALoggedInUser() {
        // Given
        UserDto expectedUserDto = new UserDto("user", User.Role.USER);
        underTest = new LoginServiceImpl(userService, expectedUserDto);
        Optional<UserDto> expected = Optional.of(expectedUserDto);

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, expectedUserDto), underTest);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetLoggedInUserShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new LoginServiceImpl(userService, null), underTest);
        Mockito.verifyNoMoreInteractions(userService);
    }

}
