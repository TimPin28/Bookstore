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

        div.innerHTML = `
        <h3>Order #${order.orderId}</h3>
        <span class="order-status ${order.status}">
            ${order.status}
        </span>
        <p>Total: $${order.totalAmount}</p>
        <ul>
            ${order.items.map(item => `
                <li>
                    ${item.bookTitle} × ${item.quantity}
                </li>
            `).join("")}
        </ul>
    `;

        container.appendChild(div);
    });
}

loadOrders();
