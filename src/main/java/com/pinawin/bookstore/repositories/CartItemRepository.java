package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndBook(User user, Book book);

    List<CartItem> findByUser(User user);
}

