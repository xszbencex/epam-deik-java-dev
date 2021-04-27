package com.epam.training.webshop.core.user.impl;

import com.epam.training.webshop.core.user.model.RegistrationDto;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class UserServiceImplTest {

    private UserServiceImpl underTest;

    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        underTest = new UserServiceImpl(userRepository);
    }

    @Test
    public void testRegisterUserShouldThrowNullPointerExceptionWhenRegistrationDtoIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.registerUser(null));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRegisterUserShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.registerUser(new RegistrationDto(null, "pass")));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRegisterUserShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.registerUser(new RegistrationDto("user", null)));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRegisterUserShouldCallUserRepositoryWhenTheInputIsValid() {
        // Given
        RegistrationDto registrationDto = Mockito.mock(RegistrationDto.class);
        Mockito.when(registrationDto.getUsername()).thenReturn("user");
        Mockito.when(registrationDto.getPassword()).thenReturn("pass");

        // When
        underTest.registerUser(registrationDto);

        // Then
        Mockito.verify(registrationDto, Mockito.times(2)).getUsername();
        Mockito.verify(registrationDto, Mockito.times(2)).getPassword();
        Mockito.verify(userRepository).save(new User(null, "user", "pass", User.Role.USER));
        Mockito.verifyNoMoreInteractions(userRepository, registrationDto);
    }

    @Test
    public void testRetrieveUserByUsernameAndPasswordShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.retrieveUserByUsernameAndPassword(null, "pass"));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRetrieveUserByUsernameAndPasswordShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.retrieveUserByUsernameAndPassword("user", null));

        // Then
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRetrieveUserByUsernameAndPasswordShouldReturnAUserDtoWhenTheInputsAreValidAndTheUserExists() {
        // Given
        Optional<User> user = Optional.of(new User(1, "user", "pass", User.Role.USER));
        Mockito.when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(user);
        Optional<UserDto> expected = Optional.of(new UserDto("user", User.Role.USER));

        // When
        Optional<UserDto> actual = underTest.retrieveUserByUsernameAndPassword("user", "pass");

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository).findByUsernameAndPassword("user", "pass");
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testRetrieveUserByUsernameAndPasswordShouldReturnAnOptionalEmptyWhenTheInputsAreValidButTheUserDoesNotExist() {
        // Given
        Mockito.when(userRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.empty());
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.retrieveUserByUsernameAndPassword("user", "pass");

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository).findByUsernameAndPassword("user", "pass");
        Mockito.verifyNoMoreInteractions(userRepository);
    }

}
