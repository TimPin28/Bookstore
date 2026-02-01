package com.pinawin.bookstore.services;

import com.pinawin.bookstore.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the Spring Security UserDetailsService.
 * This service is responsible for loading user-specific data during the 
 * authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor-based injection of the UserRepository.
     * @param userRepository The repository used to query the user table.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username provided during login.
     * This method is called internally by Spring Security's AuthenticationManager.
     * @param username The username identifying the user whose data is required.
     * @return A UserDetails object (the User entity) containing credentials and authorities.
     * @throws UsernameNotFoundException If the user does not exist in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Look for the user in the database using the unique username field
        return userRepository.findByuserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

