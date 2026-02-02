package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for handling user login attempts.
 * This class encapsulates the credentials sent by the client to the 
 * server to request authentication.
 */
@Getter
@Setter
public class LoginRequest {
    /**
     * The username provided by the user in the login form.
     */
    private String userName;

    /**
     * The plain-text password provided by the user.
     * This is used by the AuthenticationManager to verify identity.
     */
    private String password;
}
