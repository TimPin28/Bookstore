/**
 * Profile & Order History Controller
 * Fetches the authenticated user's historical transactions and renders
 * them into a detailed list of order cards.
 */
async function loadOrders() {

    /**
     * 1. Data Retrieval
     * Requests the list of orders from the backend.
     * Uses session-based credentials to identify the current user.
     */
    const response = await fetch("/api/orders", {
        credentials: "include"
    });

    // Guard Clause: Ensure only authenticated users can access the profile data
    if (!response.ok) {
        alert("Please login to view orders");
        window.location.href = "login.html";
        return;
    }

    /**
     * 2. UI Container Setup
     * Parses the JSON array of OrderResponse DTOs.
     */
    const orders = await response.json();
    const container = document.getElementById("ordersContainer");

    // Handle empty state
    if (orders.length === 0) {
        container.innerHTML = "<p>No orders yet.</p>";
        return;
    }

    /**
     * 3. Nested Rendering Logic
     * Iterates through each order and builds a card structure.
     */
    orders.forEach(order => {
        const div = document.createElement("div");
        // Hook for CSS styling (flexbox/grid)
        div.className = "order-card"; 

        div.innerHTML = `
        <div class="order-header">
            <h3>Order #${order.orderId}</h3>
            <span class="order-status ${order.status}">
                ${order.status}
            </span>
        </div>
        <ul class="order-items">
            ${order.items.map(item => `
                <li>
                    <span>${item.bookTitle} × ${item.quantity}</span>
                </li>
            `).join("")}
        </ul>
        <p class="order-total">Total: $${order.totalAmount.toFixed(2)}</p>
    `;

        container.appendChild(div);
    });
}

// Automatic execution upon script load
loadOrders();
