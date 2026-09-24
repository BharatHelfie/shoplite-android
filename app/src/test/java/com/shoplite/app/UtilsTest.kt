package com.shoplite.app

import com.shoplite.app.network.model.CartItem
import com.shoplite.app.network.model.Product
import com.shoplite.app.utils.calc
import com.shoplite.app.utils.fix
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    private fun product(id: Int, rating: Double, stock: Int) = Product(
        id = id, title = "P$id", description = "", category = "c", price = 10.0,
        discountPercentage = 0.0, rating = rating, stock = stock, brand = null,
        thumbnail = "", images = null, tags = null, reviews = null, availabilityStatus = null
    )

    @Test
    fun fixWorks() {
        val result = fix(listOf(product(1, 4.5, 3), product(2, 3.0, 0), product(3, 2.0, 8)))
        assertEquals(listOf(3, 1), result.map { it.id })
    }

    @Test
    fun calcWorks() {
        val items = listOf(CartItem(1, "A", 10.0, 10.0, "", 2), CartItem(2, "B", 5.0, 0.0, "", 1))
        assertEquals(25.0, calc(items), 0.001)
    }
}
