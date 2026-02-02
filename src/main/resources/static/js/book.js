/**
 * Book Catalog Controller
 * Handles fetching the book list, searching, and filtering by category.
 */
document.addEventListener("DOMContentLoaded", () => {
    const bookGrid = document.getElementById("bookGrid");
    const searchInput = document.getElementById("searchInput");
    const categoryInput = document.getElementById("categoryInput");


    // --- 1. Initial Load ---
    // Automatically populates the grid with all available books on page load.
    loadBooks();

    /**
     * --- 2. Helper: Render Books to UI ---
     * Dynamically builds the HTML grid from a book array.
     * @param {Array} books - The array of book objects from the API.
     */
    function renderBooks(books) {
        // Clear grid before rendering new items
        bookGrid.innerHTML = ""; 

        if (books.length === 0) {
            bookGrid.innerHTML = "<p>No books found.</p>";
            return;
        }

        books.forEach(book => {
            const card = document.createElement("div");
            card.className = "book-card";

            // If stock > 0, show button. Otherwise, show 'Out of Stock' text.
            const actionHtml = book.stock > 0
                ? `<button class="add-to-cart-btn">Add to Cart</button>`
                : `<p class="out-of-stock-label">Out of Stock</p>`;

            // Template literal for book details
            card.innerHTML = `
                <h3>${book.title}</h3>
                <p><strong>Author:</strong> ${book.author}</p>
                <p><strong>Category:</strong> ${book.category}</p>
                <p><strong>Price:</strong> $${Number(book.price).toFixed(2)}</p>
                <p><strong>Stock:</strong> ${book.stock}</p>
                <p><strong>Description:</strong> ${book.description || 'No description available.'}</p>
                ${actionHtml}
            `;

            // Only attach listener if the button exists
            const btn = card.querySelector(".add-to-cart-btn");
            if (btn) {
                btn.addEventListener("click", () => addToCart(book.id));
            }

            bookGrid.appendChild(card);
        });
    }

    /**
     * --- 3. API Call: Get All Books ---
     * Standard fetch to retrieve the full catalog.
     */
    async function loadBooks() {
        try {
            const response = await fetch("/api/books");
            const books = await response.json();
            renderBooks(books);
        } catch (error) {
            console.error("Error loading all books:", error);
        }
    }

    /**
     * --- 4. Event Listener: Title Search ---
     * Triggers as the user types (real-time).
     */
    searchInput.addEventListener("input", async () => {
        const keyword = searchInput.value.trim();

        if (keyword === "") {
            // Revert to full list if input is cleared
            loadBooks();
            return;
        }

        try {
            const response = await fetch(`/api/books/search?keyword=${encodeURIComponent(keyword)}`);
            const books = await response.json();
            renderBooks(books);
        } catch (error) {
            console.error("Error searching titles:", error);
        }
    });

    /**
     * --- 5. Event Listener: Category Search ---
     * Filters books based on the category input value.
     */
    categoryInput.addEventListener("input", async () => {
        const categoryValue = categoryInput.value.trim();

        if (categoryValue === "") {
            // Revert to full list if input is cleared
            loadBooks();
            return;
        }

        try {
            // Ensure the parameter name 'category' matches @RequestParam in BookController
            const response = await fetch(`/api/books/category?category=${encodeURIComponent(categoryValue)}`);

            if (response.ok) {
                const books = await response.json();
                console.log("Category search results:", books);
                renderBooks(books);
            } else {
                console.error("Server returned an error for category search");
            }
        } catch (error) {
            console.error("Fetch error for category:", error);
        }
    });

    /**
     * --- 6. Button: Add To Cart ---
     * Communicates with CartController.
     * @param {number} bookId - ID of the selected book.
     */
    async function addToCart(bookId) {
        const response = await fetch(`/api/cart/add?bookId=${bookId}`, {
            method: "POST",
            // Passes the JSESSIONID cookie for authentication
            credentials: "include"
        });

        if (response.ok) {
            alert("Added to cart!");
        } else if (response.status === 401 || response.status === 403) {
            alert("Please login first");
            window.location.href = "login.html";
        } else {
            alert("Failed to add to cart");
        }
    }
});