package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service class for managing book-related business logic.
 * This class serves as an intermediary between the BookController 
 * and the BookRepository.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Constructor-based dependency injection for BookRepository.
     * @param bookRepository The repository used for database interactions.
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Retrieves a paginated slice of all available books from the database.
     * @param page The zero-based page index to retrieve.
     * @param size The number of items per page.
     * @return A Page object containing the requested slice of Book entities and metadata.
     */
    public Page<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    /**
     * Searches for books based on a keyword in the title.
     * @param keyword The title snippet to search for.
     * @param page The zero-based page index to retrieve.
     * @param size The number of items per page.
     * @return A Page object containing books matching the search criteria.
     */
    public Page<Book> searchBooks(String keyword, int page, int size) {
        Pageable  pageable = PageRequest.of(page, size);
        return bookRepository.findByTitleContainingIgnoreCase(keyword,  pageable);
    }

    /**
     * Filters the catalog by a specific book category with pagination support.
     * @param category The category name (e.g., "Fiction", "Science").
     * @param page The zero-based page index to retrieve.
     * @param size The number of items per page.
     * @return A Page object containing books belonging to the specified category.
     */
    public Page<Book> filterByCategory(String category, int page, int size) {
        Pageable  pageable = PageRequest.of(page, size);
        return bookRepository.findByCategoryContainingIgnoreCase(category,  pageable);
    }

}

