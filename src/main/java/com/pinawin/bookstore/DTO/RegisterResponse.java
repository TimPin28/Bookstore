package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration requests.
 * This class captures the necessary details from the frontend registration form
 * to create a new user account in the system.
 */
@Getter
@Setter
public class RegisterResponse {

    /**
     * The unique username the user wishes to use for their account.
     */
    private String userName;

    /**
     * The email address associated with the new account. 
     * The system validates that this email is unique before saving.
     */
    private String email;

    /**
     * The plain-text password chosen by the user. 
     * This will be encoded using BCrypt before being stored in the database.
     */
    private String password;

}
