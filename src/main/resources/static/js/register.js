document.getElementById("registerForm").addEventListener("submit", async e => {

    e.preventDefault();

    const form = new FormData(e.target);

    const formData = {
        userName: form.get("userName"),
        email: form.get("email"),
        password: form.get("password"),
    }

    const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData)
    });

    if (response.ok) {
        alert("Registration successful!");
        window.location.href = "login.html";
    } else {
        alert("Registration failed");
    }
});