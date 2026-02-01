/**
 * Shopping Cart & Checkout Controller
 * Manages the retrieval of cart data, real-time total calculations, 
 * and the final checkout transaction.
 */
document.addEventListener("DOMContentLoaded", async () => {
    const cartBody = document.getElementById("cartBody");
    const totalPriceSpan = document.getElementById("totalPrice");

    /**
     * 1. Initial Cart Retrieval
     * Fetches the current user's cart items from the backend on page load.
     */
    try {
        // Explicitly include session cookies (JSESSIONID) to identify the user
        const response = await fetch("/api/cart", {
            credentials: "include"
        });

        if (response.ok) {
            const items = await response.json();
            renderCart(items);
        } else if (response.status === 401) {
            // Redirect to login if the user session has expired or is missing
            alert("Please login to view your cart");
            window.location.href = "login.html";
        }
    } catch (error) {
        console.error("Error fetching cart:", error);
    }

    /**
     * 2. UI Rendering Logic
     * Dynamically builds the cart table and calculates financial totals.
     * @param {Array} items - List of cart items containing book details and quantities.
     */
    function renderCart(items) {
        // Prevent duplicate rows on re-render
        cartBody.innerHTML = "";
        let grandTotal = 0;

        if (items.length === 0) {
            cartBody.innerHTML = "<tr><td colspan='4'>Your cart is empty.</td></tr>";
            return;
        }

        items.forEach(item => {
            // Calculate subtotal for the specific line item
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

        // Update the grand total in the UI
        totalPriceSpan.textContent = grandTotal.toFixed(2);
    }

    /**
     * 3. Checkout Execution
     * Attaches an event listener to trigger the conversion of the cart into an Order.
     */
    document.getElementById("checkoutBtn").addEventListener("click", async () => {

        // Trigger the POST request to the checkout endpoint
        const response = await fetch("/api/checkout", {
            method: "POST",
            credentials: "include"
        });

        if (response.ok) {
            alert("Order placed successfully!");

            // Redirect to index or profile after a successful transaction
            window.location.href = "index.html"; 
        } else {
            
            // Handle failures (e.g., insufficient stock or empty cart)
            alert("Checkout failed");
        }
    });

});