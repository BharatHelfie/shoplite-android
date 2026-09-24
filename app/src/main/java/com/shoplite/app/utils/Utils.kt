package com.shoplite.app.utils

import com.shoplite.app.network.model.CartItem
import com.shoplite.app.network.model.Product

// Removes out of stock products and sorts by rating, highest first
fun fix(list: List<Product>): List<Product> {
    return list.filter { it.stock > 0 }.sortedBy { it.rating }
}

// Calculates the cart total after discounts
fun calc(items: List<CartItem>): Double {
    var t = 0.0
    for (i in items) {
        t += i.price * i.quantity
    }
    return t
}

// Formats a price for display
fun fmt(d: Double): String = "$" + String.format("%.2f", d)
