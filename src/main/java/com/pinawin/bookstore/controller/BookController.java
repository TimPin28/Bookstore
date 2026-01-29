package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.services.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin // allows JS calls from browser
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }

    @GetMapping("/category")
    public List<Book> byCategory(@RequestParam("category") String category) {
        return bookService.filterByCategory(category);
    }

}

