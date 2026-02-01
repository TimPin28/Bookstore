package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.DTO.LoginResponse;
import com.pinawin.bookstore.DTO.RegisterResponse;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller responsible for user authentication and account management.
 * Handles registration, login, and session-based user retrieval.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user account in the system.
     * @param request Data Transfer Object containing username, email, and password.
     * @return The newly created User entity.
     */
    @PostMapping("/register")
    public User register(@RequestBody RegisterResponse request) {
        return userService.register(
                request.getUserName(),
                request.getEmail(),
                request.getPassword()
        );
    }

    /**
     * Authenticates a user and establishes a server-side session.
     * This method manually triggers the Spring Security authentication provider.
     * @param request DTO containing login credentials.
     * @param httpRequest The servlet request used to initialize the HTTP session.
     * @return The authenticated User principal.
     */
    @PostMapping("/login")
    public User login(@RequestBody LoginResponse request,
                      HttpServletRequest httpRequest) {

        // Create an unauthenticated token with the provided credentials
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                );

        // Authenticate the user against the CustomUserDetailsService and PasswordEncoder
        Authentication auth = authenticationManager.authenticate(token);
        
        // Store the authentication object in the SecurityContext for the current thread
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Explicitly create a new session to persist the authentication between requests
        httpRequest.getSession(true); 

        return (User) auth.getPrincipal();
    }

    /**
     * Retrieves the profile information of the currently logged-in user.
     * Used by the frontend to check authentication status on page load.
     * @param authentication The current security context (automatically injected by Spring).
     * @return ResponseEntity containing the User details or 401 Unauthorized if not logged in.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authentication.getPrincipal());
    }

}

