/**
 * Global authentication guard.
 * Checks the session status and toggles visibility between guest and user sections.
 */
document.addEventListener("DOMContentLoaded", async () => {
    // Select containers defined in navbar/header
    const guestSection = document.getElementById("auth-guest");
    const userSection = document.getElementById("auth-user");
    const logoutBtn = document.getElementById("logoutBtn");

    try {
        /**
         * 1. Session Verification
         * Sends a request to the server with session credentials included.
         * The server validates the JSESSIONID cookie.
         */
        const response = await fetch("/api/auth/me", { credentials: "include" });

        if (response.ok) {
            
            // User is authenticated
            const user = await response.json();

            // Logic: Hide login/register containers, show profile/logout containers
            if(guestSection) guestSection.style.display = "none";
            if(userSection) userSection.style.display = "inline";

            const adminLink = document.getElementById("adminLink");
            if (adminLink && user.role === "ROLE_ADMIN") {
                adminLink.style.display = "inline";
            }

            console.log("Logged in as:", user.userName);
        } else {

            // User is a guest (No active session)
            if(guestSection) guestSection.style.display = "inline";
            if(userSection) userSection.style.display = "none";
        }
    } catch (error) {
        console.error("Auth check failed:", error);
    }

    /**
     * 2. Universal Logout Handler
     * Attaches an event listener to the logout button to terminate the session.
     */
    if (logoutBtn) {
        logoutBtn.addEventListener("click", async () => {
            const res = await fetch("/api/auth/logout", {
                method: "POST",
                credentials: "include"
            });
            if (res.ok) {
                alert("Logged out successfully!");
                // Redirect to homepage to refresh UI state
                window.location.href = "index.html";
            } else {
                alert("Logout failed. Please try again.");
            }
        });
    }
});