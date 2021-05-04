package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.user.LoginService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import org.springframework.shell.Availability;

import java.util.Optional;

public abstract class AbstractAuthenticatedCommand {

    private final LoginService loginService;

    public AbstractAuthenticatedCommand(LoginService loginService) {
        this.loginService = loginService;
    }

    protected Availability loggedIn() {
        return isLoggedIn() ? Availability.available() : Availability.unavailable("You need to login first");
    }

    protected Availability admin() {
        return isLoggedIn() && isAdmin() ? Availability.available() : Availability.unavailable("You are not an admin user");
    }

    protected UserDto getUserDto() {
        return loginService.getLoggedInUser().get();
    }

    private boolean isLoggedIn() {
        return loginService.getLoggedInUser().isPresent();
    }

    private boolean isAdmin() {
        return loginService.getLoggedInUser().get().getRole() == User.Role.ADMIN;
    }

}
