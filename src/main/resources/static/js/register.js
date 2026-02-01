/**
 * User Registration Controller
 * Manages the submission of new user credentials to the authentication API.
 */
document.getElementById("registerForm").addEventListener("submit", async e => {

    // Prevent the default browser form submission to allow for an async fetch call
    e.preventDefault();

    // Use FormData API to easily extract values from the form inputs
    const form = new FormData(e.target);

    // Construct the payload to match the RegisterRequest DTO on the backend
    const formData = {
        userName: form.get("userName"),
        email: form.get("email"),
        password: form.get("password"),
    }

    /**
     * Account Creation Request
     * Sends user data to the registration endpoint.
     */
    const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // Convert the JavaScript object into a JSON string for transmission
        body: JSON.stringify(formData)
    });

    if (response.ok) {
        // Upon success, notify the user and redirect to the login page
        alert("Registration successful!");
        window.location.href = "login.html";
    } else {
        // Handle failure
        alert("Registration failed");
    }
});