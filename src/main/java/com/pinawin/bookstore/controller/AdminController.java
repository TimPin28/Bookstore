package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.DTO.RegisterRequest;
import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Administrative Controller restricted to users with ROLE_ADMIN.
 * Handles sensitive operations including inventory management and
 * elevated user registration.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BookRepository bookRepository;
    private final UserService userService;

    public AdminController(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    /**
     * Requirement: "Add new books"
     * POST /api/admin/books
     */
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // Saves the new book entity to the MySQL database
        return ResponseEntity.ok(bookRepository.save(book));
    }

    /**
     * Requirement: "Register new user" (from Admin context)
     * POST /api/admin/users
     */
    @PostMapping("/users")
    public ResponseEntity<User> registerUserByAdmin(@RequestBody RegisterRequest request) {
        // Uses the updated UserService that now supports role assignment
        return ResponseEntity.ok(userService.registerUser(request));
    }
}