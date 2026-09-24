package com.shoplite.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shoplite.app.network.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getById(id: Int): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
