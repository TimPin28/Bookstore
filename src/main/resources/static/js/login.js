/**
 * Login Controller
 * Handles user authentication by submitting credentials to the Spring Security backend.
 */
document.getElementById("loginForm").addEventListener("submit", async e => {

    // Prevent the default form submission (page reload)
    e.preventDefault();

    /**
     * Authentication Request
     * Sends the username and password to the AuthController.
     */
    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // credentials: "include" ensures the browser stores the JSESSIONID cookie
        // returned by the server upon successful authentication.
        credentials: "include",
        body: JSON.stringify({
            // Maps to LoginRequest.java
            userName: userName.value,
            password: password.value
        })
    });

    if (response.ok) {
        // Upon 200 OK, the session is established
        alert("Login successful!");

        // Redirect the user to the book catalog
        window.location.href = "books.html";
    } else {
        // Handle 401 Unauthorized or other failure statuses
        alert("Invalid credentials");
    }
});