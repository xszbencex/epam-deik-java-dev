package com.epam.training.webshop.core.user;

import com.epam.training.webshop.core.user.model.RegistrationDto;
import com.epam.training.webshop.core.user.model.UserDto;

import java.util.Optional;

public interface UserService {

    void registerUser(RegistrationDto registrationDto);

    Optional<UserDto> retrieveUserByUsernameAndPassword(String username, String password);

}
