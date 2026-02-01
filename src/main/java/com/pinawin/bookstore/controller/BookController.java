package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing book-related operations.
 * This controller provides the entry points for browsing the catalog, 
 * searching by title, and filtering by category. 
 */
@RestController
@RequestMapping("/api/books")
@CrossOrigin // Enables Cross-Origin Resource Sharing for frontend integration
public class BookController {

    private final BookService bookService;

    /**
     * Constructor-based dependency injection for BookService.
     * @param bookService The service layer handling book business logic.
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a complete list of all books available in the bookstore.
     * Maps to GET /api/books.
     * @return A list of Book entities.
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Searches for books whose titles contain the specified keyword.
     * Maps to GET /api/books/search?keyword=...
     * @param keyword The search term provided by the user.
     * @return A list of Book entities matching the title search.
     */
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }

    /**
     * Filters the book catalog based on a specific category.
     * Maps to GET /api/books/category?category=...
     * @param category The category name to filter by.
     * @return A list of Book entities belonging to the specified category.
     */
    @GetMapping("/category")
    public List<Book> byCategory(@RequestParam("category") String category) {
        return bookService.filterByCategory(category);
    }

}

