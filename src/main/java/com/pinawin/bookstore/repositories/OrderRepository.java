package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Order entities.
 * Provides data access methods to store and retrieve customer purchase 
 * records from the database.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders associated with a specific user.
     * This method is essential for displaying a personalized order 
     * history in the user's profile.
     * @param user The user whose order history is being requested.
     * @return A list of Order entities placed by the specified user.
     */
    List<Order> findByUser(User user);
}

