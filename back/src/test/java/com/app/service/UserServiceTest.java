package com.app.service;

import com.app.entity.User;
import com.app.entity.dto.UserLoginRequest;
import com.app.exception.UserAlreadyExistsException;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_WhenValidRequest_ShouldSaveUserWithEncodedPassword() {
        // Given
        UserLoginRequest request = new UserLoginRequest("testuser", "password123");
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // When
        userService.createUser(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getPassword()).isNotEqualTo("password123"); // Hasło powinno być zahaszowane
        assertThat(savedUser.getPassword()).startsWith("$2a$"); // Prefix BCrypt
    }

    @Test
    void createUser_WhenUserAlreadyExists_ShouldThrowException() {
        // Given
        UserLoginRequest request = new UserLoginRequest("existingUser", "password123");
        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Username already exists");
        
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WhenUsernameIsEmpty_ShouldThrowException() {
        // Given
        UserLoginRequest request = new UserLoginRequest("", "pass");

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username and password must not be empty");
        
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WhenPasswordIsEmpty_ShouldThrowException() {
        // Given
        UserLoginRequest request = new UserLoginRequest("user", "");

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username and password must not be empty");
        
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void createUser_WhenUsernameIsNull_ShouldThrowException() {
        // Given
        UserLoginRequest request = new UserLoginRequest(null, "pass");

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username and password must not be empty");
        
        verify(userRepository, never()).save(any());
    }
}
