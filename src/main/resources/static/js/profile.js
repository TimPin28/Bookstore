async function loadOrders() {

    const response = await fetch("/api/orders", {
        credentials: "include"
    });

    if (!response.ok) {
        alert("Please login to view orders");
        window.location.href = "login.html";
        return;
    }

    const orders = await response.json();
    const container = document.getElementById("ordersContainer");

    if (orders.length === 0) {
        container.innerHTML = "<p>No orders yet.</p>";
        return;
    }

    orders.forEach(order => {
        const div = document.createElement("div");
        div.className = "order-card"; // class for styling

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

loadOrders();
