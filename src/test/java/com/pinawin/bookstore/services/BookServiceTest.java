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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    private final int page = 0;
    private final int size = 8;

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
    @DisplayName("Should return a Page of books from repository")
    void testGetAllBooks() {
        // Arrange: Mock the repository to return a page containing our sample book
        List<Book> bookList = List.of(sampleBook);
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        // Act: Call service with pagination params
        Page<Book> result = bookService.getAllBooks(page, size);

        // Assert: Access content via result.getContent()
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Java Programming", result.getContent().getFirst().getTitle());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should filter books by title keyword")
    void testSearchBooks() {
        // Arrange
        String keyword = "Java";
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findByTitleContainingIgnoreCase(eq(keyword), any(Pageable.class)))
                .thenReturn(bookPage);

        // Act
        Page<Book> result = bookService.searchBooks(keyword, page, size);

        // Assert
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().getFirst().getTitle().contains(keyword));
    }

    @Test
    @DisplayName("Should filter books by category name")
    void testFilterByCategory() {
        // Arrange
        String category = "Technology";
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findByCategoryContainingIgnoreCase(eq(category), any(Pageable.class)))
                .thenReturn(bookPage);

        // Act
        Page<Book> result = bookService.filterByCategory(category, page, size);

        // Assert
        assertEquals("Technology", result.getContent().getFirst().getCategory());
        verify(bookRepository, atLeastOnce()).findByCategoryContainingIgnoreCase(eq(category), any(Pageable.class));
    }

    @Test
    @DisplayName("Should find books by category regardless of string casing")
    void testGetByCategory_CaseInsensitive() {
        // Arrange
        String category = "fiction";
        Page<Book> bookPage = new PageImpl<>(List.of(new Book()));
        when(bookRepository.findByCategoryContainingIgnoreCase(eq("fiction"), any(Pageable.class)))
                .thenReturn(bookPage);

        // Act
        Page<Book> result = bookService.filterByCategory(category, page, size);

        // Assert
        assertFalse(result.isEmpty());
        verify(bookRepository, times(1)).findByCategoryContainingIgnoreCase(eq("fiction"), any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty Page when no books match search keyword")
    void testSearchBooks_NoResults() {
        // Arrange
        String keyword = "NonExistentBook";
        Page<Book> emptyPage = new PageImpl<>(List.of()); // Empty list

        when(bookRepository.findByTitleContainingIgnoreCase(eq(keyword), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        Page<Book> result = bookService.searchBooks(keyword, 0, 8);

        // Assert
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
