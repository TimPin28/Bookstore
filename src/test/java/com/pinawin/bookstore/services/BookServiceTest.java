package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService using Mockito.
 * Tests catalog retrieval, search functionality, and category filtering.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    public void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Java Programming");
        sampleBook.setAuthor("Timothy Pinawin");
        sampleBook.setCategory("Technology");
        sampleBook.setPrice(new BigDecimal("29.99"));
    }

    @Test
    @DisplayName("Should return all books from repository")
    void testGetAllBooks() {
        // Arrange: Mock the repository to return a list containing our sample book
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        // Act: Call the service method
        List<Book> result = bookService.getAllBooks();

        // Assert: Verify the results
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Java Programming", result.getFirst().getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter books by title keyword")
    void testSearchBooks() {
        // Arrange
        String keyword = "Java";
        when(bookRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(List.of(sampleBook));

        // Act
        List<Book> result = bookService.searchBooks(keyword);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.getFirst().getTitle().contains(keyword));
    }

    @Test
    @DisplayName("Should filter books by category name")
    void testFilterByCategory() {
        // Arrange
        String category = "Technology";
        when(bookRepository.findByCategoryContainingIgnoreCase(category)).thenReturn(List.of(sampleBook));

        // Act
        List<Book> result = bookService.filterByCategory(category);

        // Assert
        assertEquals("Technology", result.getFirst().getCategory());
        verify(bookRepository, atLeastOnce()).findByCategoryContainingIgnoreCase(category);
    }

    @Test
    @DisplayName("Should handle search queries with special characters")
    void testSearch_SpecialCharacters() {
        // Arrange
        String query = "Spring & Java!!!";
        when(bookRepository.findByTitleContainingIgnoreCase(query)).thenReturn(List.of());

        // Act
        List<Book> results = bookService.searchBooks(query);

        // Assert
        assertTrue(results.isEmpty());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase(query);
    }

    @Test
    @DisplayName("Should find books by category regardless of string casing")
    void testGetByCategory_CaseInsensitive() {
        // Arrange
        String category = "fiction";
        when(bookRepository.findByCategoryContainingIgnoreCase("fiction")).thenReturn(List.of(new Book()));

        // Act
        List<Book> results = bookService.filterByCategory(category);

        // Assert
        assertFalse(results.isEmpty());
        verify(bookRepository, times(1)).findByCategoryContainingIgnoreCase("fiction");
    }
}
