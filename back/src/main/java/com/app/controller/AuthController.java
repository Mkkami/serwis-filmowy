package com.app.controller;

import com.app.entity.dto.UserLoginRequest;
import com.app.exception.UserAlreadyExistsException;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserLoginRequest user) {
        try {
            userService.createUser(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
        return ResponseEntity.status(201).body("User registered");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginRequest login) {
//
//        return ResponseEntity.ok("Login successful");
//    }

}
