package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.DTO.LoginRequest;
import com.pinawin.bookstore.DTO.RegisterRequest;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.getUserName(),
                request.getEmail(),
                request.getPassword()
        );
    }


    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request,
                      HttpServletRequest httpRequest) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                );

        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        httpRequest.getSession(true); // 🔑 create session

        return (User) auth.getPrincipal();
    }

}

