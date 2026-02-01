package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // Simulate a successful register
    @Test
    void register_successful() {
        // 1. Arrange
        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password"))
                .thenReturn("hashed");

        // We stub the save so we don't get a NullPointerException
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act
        User user = userService.register(
                "testUser",
                "test@email.com",
                "password"
        );

        // 3. Assert
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("hashed", user.getPassword());
        // verify ensures the save actually happened in the database context
        verify(userRepository).save(any(User.class));
    }

    // Simulate registering with duplicate email
    @Test
    void register_duplicateEmail_throwsException() {
        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () ->
                userService.register(
                        "testUser",
                        "test@email.com",
                        "password"
                )
        );
    }
    
}
