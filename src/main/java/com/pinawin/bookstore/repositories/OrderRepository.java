package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

