package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.user.LoginService;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.RegistrationDto;
import com.epam.training.webshop.core.user.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@ShellComponent
public class UserCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCommand.class);

    private final LoginService loginService;
    private final UserService userService;

    public UserCommand(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @ShellMethod(value = "User Login", key = "user login")
    public String login(String username, String password) {
        return handleErrorScenario(() -> loginService.login(username, password),
                (userDto) -> userDto.toString(),
                "Wrong username or password");
    }

    @ShellMethod(value = "User Logout", key = "user logout")
    public String logout() {
        return handleErrorScenario(() -> loginService.logout(),
                (userDto) -> userDto.getUsername() + " is logged out",
                "You need to login first");
    }

    @ShellMethod(value = "User Print", key = "user print")
    public String printLoggedInUser() {
        return handleErrorScenario(() -> loginService.getLoggedInUser(),
                (userDto) -> userDto.toString(),
                "You need to login first");
    }

    @ShellMethod(value = "User Registrate", key = "user registrate")
    public String registrateUser(String username, String password) {
        RegistrationDto registrationDto = new RegistrationDto(username, password);
        String message;
        try {
            userService.registerUser(registrationDto);
            message = "Registration was successful";
        } catch (Exception e) {
            LOGGER.error("Error during registration", e);
            message = "The registration failed";
        }

        return message;
    }

    private String handleErrorScenario(Supplier<Optional<UserDto>> supplier, Function<UserDto, String> successfulMessageProvider, String errorMessage) {
        Optional<UserDto> userDto = supplier.get();
        String message;
        if (userDto.isPresent()) {
            message = successfulMessageProvider.apply(userDto.get());
        } else {
            message = errorMessage;
        }
        return message;
    }

}
