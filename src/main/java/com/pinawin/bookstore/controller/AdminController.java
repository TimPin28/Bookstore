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
 * This controller handles elevated operations including manual inventory
 * management and the registration of new users with specific roles.
 * Access is governed by the SecurityConfig's role-based authorization rules.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BookRepository bookRepository;
    private final UserService userService;

    /**
     * Constructor-based dependency injection for required repositories and services.
     * @param bookRepository The data access object for Book entities.
     * @param userService The service handling user logic and role assignment.
     */
    public AdminController(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    /**
     * Persists a new book entity to the catalog.
     * Maps to POST /api/admin/books.
     * @param book The Book model containing title, author, price, and stock data.
     * @return A ResponseEntity containing the successfully persisted Book.
     */
    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // Saves the new book entity to the MySQL database
        return ResponseEntity.ok(bookRepository.save(book));
    }

    /**
     * Facilitates the creation of new user accounts from an administrative context,
     * allowing for explicit role assignment (e.g., creating additional admins).
     * Maps to POST /api/admin/users.
     * @param request The DTO containing username, email, password, and the target role.
     * @return A ResponseEntity containing the newly created User entity.
     */
    @PostMapping("/users")
    public ResponseEntity<User> registerUserByAdmin(@RequestBody RegisterRequest request) {
        // Maps DTO fields to the specialized UserService registration logic
        User newUser = userService.register(
                request.getUserName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        return ResponseEntity.ok(newUser);
    }
}