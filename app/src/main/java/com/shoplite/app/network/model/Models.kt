package com.shoplite.app.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Product(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPercentage") val discountPercentage: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("brand") val brand: String?,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("reviews") val reviews: List<Review>?,
    @SerializedName("availabilityStatus") val availabilityStatus: String?,
)

data class Review(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String,
)

data class ProductsResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int,
)

data class LoginResponse(
    val id: Int,
    val username: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val image: String?,
    val accessToken: String?,
    val refreshToken: String?,
)

data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int,
)

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val image: String?,
)

data class AddCartRequest(
    val userId: Int,
    val products: List<AddCartProduct>,
)

data class AddCartProduct(
    val id: Int,
    val quantity: Int,
)

data class CartResponse(
    val id: Int,
    val total: Double,
    val discountedTotal: Double,
    val totalProducts: Int,
)

data class CartItem(
    val productId: Int,
    val title: String,
    val price: Double,
    val discountPercentage: Double,
    val thumbnail: String,
    var quantity: Int,
)
