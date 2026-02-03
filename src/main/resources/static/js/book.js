/**
 * Book Catalog Controller
 * Handles fetching the book list, searching, and filtering by category.
 */
document.addEventListener("DOMContentLoaded", () => {
    const bookGrid = document.getElementById("bookGrid");
    const searchInput = document.getElementById("searchInput");
    const categoryInput = document.getElementById("categoryInput");

    // --- Pagination State ---
    let currentPage = 0;
    const pageSize = 8;

    // Automatically populates the grid with all available books on page load.
    loadBooks(0);

    /**
     * --- 2. Helper: Render Books to UI ---
     * Dynamically builds the HTML grid from a book array.
     * @param {Array} books - The array of book objects from the API.
     */
    function renderPage(pageData) {
        const books = pageData.content
        // Clear grid before rendering new items
        bookGrid.innerHTML = ""; 

        if (!books || books.length === 0) {
            bookGrid.innerHTML = "<p>No books found.</p>";
            // Allow render controls to go back
            renderControls(pageData);
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
        renderControls(pageData);
    }

    async function searchBooks(keyword, page) {
        try {
            const response = await fetch(`/api/books/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${pageSize}`);
            const data = await response.json();
            renderPage(data); // This now handles the Page object
        } catch (error) {
            console.error("Error searching books:", error);
        }
    }

    async function filterByCategory(category, page) {
        try {
            const response = await fetch(`/api/books/category?category=${encodeURIComponent(category)}&page=${page}&size=${pageSize}`);
            const data = await response.json();
            renderPage(data);
        } catch (error) {
            console.error("Error filtering category:", error);
        }
    }

    function renderControls(pageData) {
        let controls = document.getElementById("paginationControls");

        if (!controls) {
            controls = document.createElement("div");
            controls.id = "paginationControls";
            controls.className = "pagination-container";
            bookGrid.after(controls); // Place it right after the grid
        }

        controls.innerHTML = `
            <button ${pageData.first ? 'disabled' : ''} id="prevBtn">Previous</button>
            <span>Page ${pageData.number + 1} of ${pageData.totalPages}</span>
            <button ${pageData.last ? 'disabled' : ''} id="nextBtn">Next</button>
        `;

        document.getElementById("prevBtn").onclick = () => {
            currentPage--;
            executeFetch();
        };

        document.getElementById("nextBtn").onclick = () => {
            currentPage++;
            executeFetch();
        };
    }

    function executeFetch() {
        const keyword = searchInput.value.trim();
        const category = categoryInput.value.trim();

        if (keyword) {
            searchBooks(keyword, currentPage);
        }
        else if (category) {
            filterByCategory(keyword, currentPage);
        }
        else {
            loadBooks(currentPage);
        }
    }

    /**
     * --- 3. API Call: Get All Books ---
     * Standard fetch to retrieve the full catalog.
     */
    async function loadBooks(page) {
        try {
            const response = await fetch(`/api/books?page=${page}&size=${pageSize}`);
            const data = await response.json();
            renderPage(data);
        } catch (error) {
            console.error("Error loading books:", error);
        }
    }

    /**
     * --- 4. Event Listener: Title Search ---
     * Triggers as the user types (real-time).
     */
    searchInput.addEventListener("input", async () => {
        const keyword = searchInput.value.trim();
        currentPage = 0;

        if (keyword === "") {
            // Revert to full list if input is cleared
            loadBooks(0);
            return;
        }

        searchBooks(keyword, currentPage);
    });

    /**
     * --- 5. Event Listener: Category Search ---
     * Filters books based on the category input value.
     */
    categoryInput.addEventListener("input", async () => {
        const categoryValue = categoryInput.value.trim();
        currentPage = 0;

        if (categoryValue === "") {
            // Revert to full list if input is cleared
            loadBooks(0);
            return;
        }

        filterByCategory(categoryValue, currentPage);
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