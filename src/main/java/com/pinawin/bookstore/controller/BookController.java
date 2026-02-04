package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


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
     * Retrieves a paginated slice of all books available in the bookstore.
     * Maps to GET /api/books.
     * @param page The zero-based page index to retrieve (defaults to 0).
     * @param size The number of records per page (defaults to 8).
     * @return A Page object containing a subset of Book entities and pagination metadata.
     */
    @GetMapping
    public Page<Book> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "8") int size) {
        return bookService.getAllBooks(page, size);
    }

    /**
     * Searches for books whose titles contain the specified keyword with pagination support.
     * Maps to GET /api/books/search?keyword=...&page=...&size=...
     * @param keyword The search term provided by the user.
     * @param page The zero-based page index to retrieve (defaults to 0).
     * @param size The number of records per page (defaults to 8).
     * @return A Page object of Book entities matching the title search.
     */
    @GetMapping("/search")
    public Page<Book> searchBooks(@RequestParam String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "8") int size) {
        return bookService.searchBooks(keyword, page, size);
    }

    /**
     * Filters the book catalog based on a specific category with pagination support.
     * Maps to GET /api/books/category?category=...&page=...&size=...
     * @param category The category name to filter by.
     * @param page The zero-based page index to retrieve (defaults to 0).
     * @param size The number of records per page (defaults to 8).
     * @return A Page object of Book entities belonging to the specified category.
     */
    @GetMapping("/category")
    public Page<Book> byCategory(@RequestParam("category") String category,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "8") int size) {
        return bookService.filterByCategory(category, page, size);
    }

}

