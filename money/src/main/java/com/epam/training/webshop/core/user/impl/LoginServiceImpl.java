package com.epam.training.webshop.core.user.impl;

import com.epam.training.webshop.core.user.LoginService;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private UserDto loggedInUser = null;

    @Autowired
    public LoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    LoginServiceImpl(UserService userService, UserDto loggedInUser) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public Optional<UserDto> login(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null during login");
        Objects.requireNonNull(password, "Password cannot be null during login");
        loggedInUser = userService.retrieveUserByUsernameAndPassword(username, password).orElseGet(() -> null);
        return getLoggedInUser();
    }

    @Override
    public Optional<UserDto> logout() {
        Optional<UserDto> previouslyLoggedInUser = getLoggedInUser();
        loggedInUser = null;
        return previouslyLoggedInUser;
    }

    @Override
    public Optional<UserDto> getLoggedInUser() {
        return Optional.ofNullable(loggedInUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginServiceImpl that = (LoginServiceImpl) o;
        return Objects.equals(userService, that.userService) && Objects.equals(loggedInUser, that.loggedInUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, loggedInUser);
    }

    @Override
    public String toString() {
        return "LoginServiceImpl{" +
                "userService=" + userService +
                ", loggedInUser=" + loggedInUser +
                '}';
    }
}
