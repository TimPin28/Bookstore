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
                window.location.href = "index.html";
            }
        });
    }
});