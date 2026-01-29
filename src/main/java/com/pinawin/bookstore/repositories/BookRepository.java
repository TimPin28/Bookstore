package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategoryContainingIgnoreCase(String category);
    List<Book> findByTitleContainingIgnoreCase (String keyword);

}

