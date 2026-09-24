package com.shoplite.app.data.repository

import android.content.Context
import android.util.Log
import com.shoplite.app.data.local.ProductDao
import com.shoplite.app.network.ApiService
import com.shoplite.app.network.model.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProductDao,
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Gets products for the given page.
    // Uses the local database as a cache so the app also works offline.
    suspend fun doIt(page: Int): List<Product> {
        if (page == 0) {
            val cached = dao.getAll()
            if (cached.isNotEmpty()) {
                return cached
            }
        }
        return try {
            val res = api.getProducts(PAGE_SIZE, page * PAGE_SIZE)
            dao.deleteAll()
            dao.insertAll(res.products)
            prefs.edit().putLong("last_sync", System.currentTimeMillis()).apply()
            res.products
        } catch (e: Exception) {
            Log.e("ProductRepository", "doIt failed", e)
            dao.getAll()
        }
    }

    // Returns a single product from the cache
    fun getOne(id: Int): Product? = dao.getById(id)

    // Returns true if the cache is older than 24 hours
    fun isOld(): Boolean {
        val last = prefs.getLong("last_sync", 0)
        return System.currentTimeMillis() - last > 24 * 60 * 60 * 1000
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
