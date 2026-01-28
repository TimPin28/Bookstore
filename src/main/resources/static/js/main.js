const books = [
    {
        id: 1,
        title: "Clean Code",
        author: "Robert C. Martin",
        price: 29.99,
        category: "Programming"
    },
    {
        id: 2,
        title: "Effective Java",
        author: "Joshua Bloch",
        price: 39.99,
        category: "Programming"
    },
    {
        id: 3,
        title: "Atomic Habits",
        author: "James Clear",
        price: 19.99,
        category: "Self-Help"
    }
];

const bookGrid = document.getElementById("bookGrid");
const searchInput = document.getElementById("searchInput");

function renderBooks(bookList) {
    bookGrid.innerHTML = "";

    if (bookList.length === 0) {
        bookGrid.innerHTML = "<p>No books found.</p>";
        return;
    }

    bookList.forEach(book => {
        const card = document.createElement("div");
        card.className = "book-card";

        card.innerHTML = `
            <h3>${book.title}</h3>
            <p><strong>Author:</strong> ${book.author}</p>
            <p><strong>Category:</strong> ${book.category}</p>
            <p><strong>Price:</strong> $${book.price}</p>
            <button>Add to Cart</button>
        `;

        bookGrid.appendChild(card);
    });
}

// Search functionality
searchInput.addEventListener("input", () => {
    const keyword = searchInput.value.toLowerCase();

    const filteredBooks = books.filter(book =>
        book.title.toLowerCase().includes(keyword) ||
        book.author.toLowerCase().includes(keyword)
    );

    renderBooks(filteredBooks);
});

// Initial render
renderBooks(books);
