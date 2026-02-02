package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user accounts and registration.
 * Handles credential security and validation of user-specific data.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection.
     * @param userRepository Repository for user data access.
     * @param passwordEncoder The BCrypt encoder defined in SecurityConfig.
     */
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * Validates that the email and userName is not already in use 
     * and hashes the password for security.
     * @param name The chosen username.
     * @param email The user's email address.
     * @param password The plain-text password from the registration form.
     * @return The saved User entity.
     * @throws RuntimeException if the email is already registered.
     */
    public User register(String name, String email, String password) {

        // Check for existing email and userName to prevent duplicate accounts
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.findByuserName(name).isPresent()) {
            throw new RuntimeException("User Name already registered");
        }

        User user = new User();
        user.setUserName(name);
        user.setEmail(email);

        // Hash the password before saving to the database.
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}

