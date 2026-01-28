const bookGrid = document.getElementById("bookGrid");
const searchInput = document.getElementById("searchInput");

async function loadBooks() {
    const response = await fetch("/api/books");
    const books = await response.json();
    renderBooks(books);
}

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
            <p><strong>Price:</strong> $${book.price}</p>
            <p><strong>Description:</strong>$${book.description}</p>
            <button>Add to Cart</button>
        `;

        bookGrid.appendChild(card);
    });
}

// Search using backend
searchInput.addEventListener("input", async () => {
    const keyword = searchInput.value.trim();

    if (keyword === "") {
        loadBooks();
        return;
    }

    const response = await fetch(`/api/books/search?keyword=${keyword}`);
    const books = await response.json();
    renderBooks(books);
});

// Initial load
loadBooks();
