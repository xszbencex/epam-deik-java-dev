package com.epam.training.webshop.core.user.impl;

import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.RegistrationDto;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.entity.User;
import com.epam.training.webshop.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(RegistrationDto registrationDto) {
        Objects.requireNonNull(registrationDto, "RegistrationDto cannot be null during registration");
        Objects.requireNonNull(registrationDto.getUsername(), "Username cannot be null during registration");
        Objects.requireNonNull(registrationDto.getPassword(), "Password cannot be null during registration");
        User user = new User(null, registrationDto.getUsername(), registrationDto.getPassword(), User.Role.USER);
        userRepository.save(user);
    }

    @Override
    public Optional<UserDto> retrieveUserByUsernameAndPassword(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null during login");
        Objects.requireNonNull(password, "Password cannot be null during login");
        return convertToDto(userRepository.findByUsernameAndPassword(username, password));
    }

    private Optional<UserDto> convertToDto(Optional<User> user) {
        Optional<UserDto> userDto = Optional.empty();
        if(user.isPresent()) {
            userDto = Optional.of(convertToDto(user.get()));
        }
        return userDto;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getUsername(), user.getRole());
    }

}
