package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Retrieves all available books from the database.
     * @return A list containing all Book entities.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Searches for books based on a keyword in the title.
     * @param keyword The title snippet to search for.
     * @return A list of books matching the search criteria.
     */
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }

    /**
     * Filters the catalog by a specific book category.
     * @param category The category name (e.g., "Fiction", "Science").
     * @return A list of books belonging to the specified category.
     */
    public List<Book> filterByCategory(String category) {
        return bookRepository.findByCategoryContainingIgnoreCase(category);
    }

}

