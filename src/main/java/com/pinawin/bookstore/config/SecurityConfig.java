package com.pinawin.bookstore.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 * This class defines the security protocols, authentication mechanisms, 
 * and authorization rules for the entire Bookstore application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

     /**
     * Defines the password hashing algorithm.
     * BCrypt is used to securely hash passwords before storing them in the database.
     * @return a BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager bean.
     * This manager is used in the AuthController to manually authenticate users 
     * during the login process.
     * @param config AuthenticationConfiguration provided by Spring.
     * @return the AuthenticationManager instance.
     * @throws Exception if manager retrieval fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the Security Filter Chain.
     * Defines which URLs are protected, manages session policies, 
     * handles CSRF, and customizes the logout behavior.
     * @param http the HttpSecurity object to configure.
     * @return the built SecurityFilterChain.
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // Ensure the SecurityContext is saved in the session
                .securityContext(context -> context
                        .requireExplicitSave(false)
                )
                
                .formLogin(form -> form.disable())

                // Custom Logout Configuration
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // Endpoint for logout requests
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Returns a 200 OK status instead of a default redirect to /login
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true) // Destroys the server-side session
                        .deleteCookies("JSESSIONID") // Clears the browser session cookie
                        .clearAuthentication(true)   // Wipes the security context
                )
                
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session if needed
                        .maximumSessions(1)
                );

        return http.build();
    }
}
