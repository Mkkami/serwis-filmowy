package com.app.service;

import com.app.entity.User;
import com.app.entity.dto.UserLoginRequest;
import com.app.exception.UserAlreadyExistsException;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createUser(UserLoginRequest loginRequest) {
        if (loginRequest.username() == null || loginRequest.username().isBlank() ||
                loginRequest.password() == null || loginRequest.password().isBlank()) {
            throw new IllegalArgumentException("Username and password must not be empty");
        }

        if (loginRequest.password().length() < 8) {
            throw new IllegalArgumentException("Password is too weak");
        }

        if (userRepository.findByUsername(loginRequest.username()) != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User user = new User();
        user.setUsername(loginRequest.username());
        user.setPassword(
                passwordEncoder.encode(loginRequest.password()));
        userRepository.save(user);
    }

}
