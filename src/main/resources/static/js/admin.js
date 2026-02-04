document.addEventListener("DOMContentLoaded", () => {

    // --- Add Book Logic ---
    const addBookForm = document.getElementById("addBookForm");
    addBookForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const bookData = {
            title: formData.get("title"),
            author: formData.get("author"),
            category: formData.get("category"),
            price: parseFloat(formData.get("price")),
            stock: parseInt(formData.get("stock")),
            description: formData.get("description")
        };

        const response = await fetch("/api/admin/books", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bookData)
        });

        if (response.ok) {
            alert("Book added successfully!");
            addBookForm.reset();
        } else {
            alert("Failed to add book. Ensure you have Admin privileges.");
        }
    });

    // --- Admin User Registration Logic ---
    const adminRegisterForm = document.getElementById("adminRegisterForm");
    adminRegisterForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);

        const userData = {
            userName: formData.get("userName"),
            email: formData.get("email"),
            password: formData.get("password"),
            role: formData.get("role")
        };

        const response = await fetch("/api/admin/users", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            alert("User registered successfully!");
            adminRegisterForm.reset();
        } else {
            const error = await response.json();
            alert("Error: " + (error.message || "Registration failed"));
        }
    });
});