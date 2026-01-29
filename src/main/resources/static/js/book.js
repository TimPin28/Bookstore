// Wait for the HTML to be fully loaded before running the script
document.addEventListener("DOMContentLoaded", () => {
    const bookGrid = document.getElementById("bookGrid");
    const searchInput = document.getElementById("searchInput");
    const categoryInput = document.getElementById("categoryInput");


    // --- 1. Initial Load ---
    loadBooks();

    // --- 2. Helper: Render Books to UI ---
    function renderBooks(books) {
        bookGrid.innerHTML = "";

        if (books.length === 0) {
            bookGrid.innerHTML = "<p>No books found.</p>";
            return;
        }

        books.forEach(book => {
            const card = document.createElement("div");
            card.className = "book-card";

            card.innerHTML = `
                <h3>${book.title}</h3>
                <p><strong>Author:</strong> ${book.author}</p>
                <p><strong>Category:</strong> ${book.category}</p>
                <p><strong>Price:</strong> $${Number(book.price).toFixed(2)}</p>
                <p><strong>Description:</strong> ${book.description || 'No description available.'}</p>
                <button class="add-to-cart-btn">Add to Cart</button>
            `;

            // Find the button inside the card
            const btn = card.querySelector(".add-to-cart-btn");

            // Attach the listener directly
            btn.addEventListener("click", () => addToCart(book.id));
            bookGrid.appendChild(card);
        });
    }

    // --- 3. API Call: Get All Books ---
    async function loadBooks() {
        try {
            const response = await fetch("/api/books");
            const books = await response.json();
            renderBooks(books);
        } catch (error) {
            console.error("Error loading all books:", error);
        }
    }

    // --- 4. Event Listener: Title Search ---
    searchInput.addEventListener("input", async () => {
        const keyword = searchInput.value.trim();

        if (keyword === "") {
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

    // --- 5. Event Listener: Category Search ---
    categoryInput.addEventListener("input", async () => {
        const categoryValue = categoryInput.value.trim();

        if (categoryValue === "") {
            loadBooks();
            return;
        }

        try {
            // Ensure the parameter name 'category' matches your @RequestParam in BookController
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

    // --- 6. Button: Add To Cart ---
    async function addToCart(bookId) {
        const response = await fetch(`/api/cart/add?bookId=${bookId}`, {
            method: "POST",
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