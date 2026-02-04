package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for Book entities.
 * Extends JpaRepository to provide standard CRUD operations and custom 
 * search functionality using Spring Data JPA method conventions.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds a paginated slice of books belonging to a specific category.
     * Uses 'Containing' and 'IgnoreCase' to allow flexible, partial,
     * and case-insensitive matches.
     * @param category The category string to search for.
     * @param pageable The pagination information (page number, size, and sorting).
     * @return A Page of books matching the category criteria, including pagination metadata.
     */
    Page<Book> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    /**
     * Searches for books based on a keyword in the title with pagination support.
     * Enables partial title matching to power the search bar functionality.
     * @param keyword The title keyword to search for.
     * @param pageable The pagination information (page number, size, and sorting).
     * @return A Page of books whose titles contain the keyword.
     */
    Page<Book> findByTitleContainingIgnoreCase (String keyword, Pageable pageable);

}

