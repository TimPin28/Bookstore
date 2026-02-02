package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomUserDetailsService.
 * Verifies that the security layer correctly retrieves users and handles
 * missing accounts according to Spring Security standards.
 */
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserName("Timothy");
        testUser.setPassword("hashed_password");
        testUser.setEmail("timothy@example.com");
    }

    @Test
    @DisplayName("Should load user details when username exists")
    void testLoadUserByUsername_Success() {
        // Arrange: Mock the repository to return our test user
        when(userRepository.findByuserName("Timothy")).thenReturn(Optional.of(testUser));

        // Act: Call the method used by Spring Security during login
        UserDetails result = userDetailsService.loadUserByUsername("Timothy");

        // Assert: Verify the returned object matches our user
        assertNotNull(result);
        assertEquals("Timothy", result.getUsername());
        assertEquals("hashed_password", result.getPassword());
        verify(userRepository, times(1)).findByuserName("Timothy");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void testLoadUserByUsername_NotFound() {
        // Arrange: Mock the repository to return an empty Optional
        when(userRepository.findByuserName("UnknownUser")).thenReturn(Optional.empty());

        // Act & Assert: Verify the specific security exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("UnknownUser");
        });
    }
}