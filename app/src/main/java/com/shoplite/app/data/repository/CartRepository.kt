package com.shoplite.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoplite.app.network.ApiService
import com.shoplite.app.network.model.AddCartProduct
import com.shoplite.app.network.model.AddCartRequest
import com.shoplite.app.network.model.CartItem
import com.shoplite.app.network.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CartRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: ApiService,
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val items: MutableList<CartItem> = load()

    // Adds a product to the cart
    suspend fun process(p: Product, qty: Int = 1): Boolean {
        val existing = items.find { it.productId == p.id }
        if (existing != null) {
            existing.quantity += qty
        } else {
            items.add(CartItem(p.id, p.title, p.price, p.discountPercentage, p.thumbnail, qty))
        }
        save()
        val userId = prefs.getInt("user_id", 1)
        val res = api.addToCart(AddCartRequest(userId, listOf(AddCartProduct(p.id, qty))))
        return res.isSuccessful
    }

    // Removes a product from the cart
    fun remove(id: Int) {
        items.removeAll { it.productId == id }
        save()
    }

    // Returns everything in the cart
    fun get(): List<CartItem> = items

    // Returns number of items in the cart
    fun size(): Int = items.sumOf { it.quantity }

    private fun load(): MutableList<CartItem> {
        val json = prefs.getString("cart", null) ?: return mutableListOf()
        return gson.fromJson(json, object : TypeToken<MutableList<CartItem>>() {}.type)
    }

    private fun save() {
        prefs.edit().putString("cart", gson.toJson(items)).apply()
    }
}
