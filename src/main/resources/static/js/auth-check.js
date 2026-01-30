document.addEventListener("DOMContentLoaded", async () => {
    const guestSection = document.getElementById("auth-guest");
    const userSection = document.getElementById("auth-user");
    const logoutBtn = document.getElementById("logoutBtn");

    try {
        // 1. Check if user is logged in via the backend
        const response = await fetch("/api/auth/me", { credentials: "include" });

        if (response.ok) {
            const user = await response.json();
            // Show user items, hide guest items
            if(guestSection) guestSection.style.display = "none";
            if(userSection) userSection.style.display = "inline";
            console.log("Logged in as:", user.userName);
        } else {
            // Show guest items, hide user items
            if(guestSection) guestSection.style.display = "inline";
            if(userSection) userSection.style.display = "none";
        }
    } catch (error) {
        console.error("Auth check failed:", error);
    }

    // 2. Universal Logout Handler
    if (logoutBtn) {
        logoutBtn.addEventListener("click", async () => {
            const res = await fetch("/api/auth/logout", {
                method: "POST",
                credentials: "include"
            });
            if (res.ok) {
                alert("Logged out successfully!");
                window.location.href = "index.html";
            } else {
                alert("Logout failed. Please try again.");
            }
        });
    }

    // 3. Login logic
    /*
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
    */

    // 4. Register Logic
    /*
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
     */
});