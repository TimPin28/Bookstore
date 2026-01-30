document.getElementById("loginForm").addEventListener("submit", async e => {
    e.preventDefault();

    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
            userName: userName.value,
            password: password.value
        })
    });

    if (response.ok) {
        alert("Login successful!");
        window.location.href = "books.html";
    } else {
        alert("Invalid credentials");
    }
});