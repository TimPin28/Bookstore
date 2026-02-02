# 📚 Timothy's Bookstore Application

A full-stack E-commerce platform built with **Java Spring Boot**, **Spring Security**, and **Vanilla JavaScript**. This application manages a book catalog, user authentication, persistent shopping carts, and order history.

---

## 🚀 Features

### **1. Security & Authentication**
* **Custom Authentication:** Uses Spring Security with a custom `UserDetailsService`.
* **Password Safety:** BCrypt salt-hashing for secure user credentials.
* **Session Management:** Role-based access control and persistent login sessions.

### **2. Catalog & Shopping**
* **Live Search:** Filter books by title or category in real-time.
* **Stock Management:** Dynamic UI that hides "Add to Cart" buttons when items are out of stock.
* **Cart Persistence:** Add, remove, and clear items within a session-aware shopping cart.

### **3. Order Processing**
* **Transactional Checkout:** Atomic operations that create orders and reduce inventory simultaneously.
* **Order History:** A personalized profile page to view past purchases.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java , Spring Boot, Spring Data JPA |
| **Security** | Spring Security (BCrypt, Session-based) |
| **Database** | MySQL |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript (Fetch API) |
| **Testing** | JUnit 5, Mockito |

---

## 📦 Project Structure

```text
src/main/java/com/pinawin/bookstore/
├── auth/           # Security configuration and custom details
├── controllers/    # REST API endpoints
├── dto/            # Data Transfer Objects for clean API responses
├── models/         # JPA Entities (Book, User, CartItem, Order)
├── repositories/   # Data Access Layer (Spring Data JPA)
└── services/       # Business Logic Layer
