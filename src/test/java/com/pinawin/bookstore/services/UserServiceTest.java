package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 * Ensures account creation logic and credential security are functioning correctly.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should successfully register a new user with a hashed password")
    void register_successful() {
        // Arrange
        String rawPassword = "password123";
        String hashed = "hashed_pw";

        // Mock both checks to return empty (meaning neither email nor username is taken)
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByuserName("timothy")).thenReturn(Optional.empty());

        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);

        // Simulate the save operation returning the object passed to it
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        User result = userService.register("timothy", "test@example.com", rawPassword);

        // Assert
        assertNotNull(result);
        assertEquals("timothy", result.getUsername()); // Matches your model field
        assertEquals("test@example.com", result.getEmail());
        assertEquals(hashed, result.getPassword());

        // Verify all critical interactions occurred exactly once
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).findByuserName("timothy");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("Should throw exception when email is already in use")
    void register_duplicateEmail_throwsException() {
        // Arrange
        User existingUser = new User();
        existingUser.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register("timothy", "test@example.com", "password");
        });

        assertEquals("Email already registered", exception.getMessage());
        // Verify that the save method was NEVER called due to the exception
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username is already taken")
    void register_duplicateUsername_throwsException() {
        // Arrange
        User existingUser = new User();
        existingUser.setUserName("timothy_p");

        // Mock the repository to find the user by name
        when(userRepository.findByuserName("timothy_p")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register("timothy_p", "new_email@example.com", "password");
        });

        assertEquals("User Name already registered", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}