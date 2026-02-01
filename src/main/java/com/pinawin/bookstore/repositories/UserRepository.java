package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 * Facilitates the storage, retrieval, and validation of user accounts within 
 * the MySQL database.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user based on their registered email address.
     * Primarily used during the registration process to ensure email uniqueness.
     * @param email The email address to look up.
     * @return An Optional containing the User if the email exists.
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user based on their unique username.
     * This method is critical for the authentication flow, as it is used by 
     * the CustomUserDetailsService to load user details for Spring Security.
     * @param username The username to look up.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByuserName(String username);
}

