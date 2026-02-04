package com.pinawin.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity representing a registered user in the bookstore.
 * Implements UserDetails to integrate directly with Spring Security's 
 * authentication and authorization framework.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username used for logging into the application.
     */
    @Column(nullable = false, name = "user_name")
    private String userName;

    /**
     * Unique email address associated with the user account.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Encrypted password string. 
     * Always stored as a BCrypt hash, never as plain text.
     */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    /**
     * List of historical orders placed by this user.
     * JsonIgnore prevents infinite recursion during API responses.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    /**
     * List of items currently in the user's persistent shopping cart.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartItems;

    /**
     * Returns the authorities granted to the user.
     * Currently defaults to "ROLE_USER" for all registered accounts.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    // --- UserDetails Account Status Methods ---
    // These are set to true by default to allow immediate access upon registration.
    
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
