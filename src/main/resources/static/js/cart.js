document.addEventListener("DOMContentLoaded", async () => {
    const cartBody = document.getElementById("cartBody");
    const totalPriceSpan = document.getElementById("totalPrice");

    try {
        const response = await fetch("/api/cart", {
            credentials: "include" // Important to send the session cookie!
        });

        if (response.ok) {
            const items = await response.json();
            renderCart(items);
        } else if (response.status === 401) {
            alert("Please login to view your cart");
            window.location.href = "login.html";
        }
    } catch (error) {
        console.error("Error fetching cart:", error);
    }

    function renderCart(items) {
        cartBody.innerHTML = "";
        let grandTotal = 0;

        if (items.length === 0) {
            cartBody.innerHTML = "<tr><td colspan='4'>Your cart is empty.</td></tr>";
            return;
        }

        items.forEach(item => {
            const subtotal = item.book.price * item.quantity;
            grandTotal += subtotal;

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${item.book.title}</td>
                <td>$${item.book.price.toFixed(2)}</td>
                <td>${item.quantity}</td>
                <td>$${subtotal.toFixed(2)}</td>
            `;
            cartBody.appendChild(row);
        });

        totalPriceSpan.textContent = grandTotal.toFixed(2);
    }
});