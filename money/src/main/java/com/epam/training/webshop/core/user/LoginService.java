package com.epam.training.webshop.core.user;

import com.epam.training.webshop.core.user.model.UserDto;

import java.util.Optional;

public interface LoginService {

    Optional<UserDto> login(String username, String password);

    Optional<UserDto> logout();

    Optional<UserDto> getLoggedInUser();

}
