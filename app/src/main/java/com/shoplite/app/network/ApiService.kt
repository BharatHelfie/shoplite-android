package com.shoplite.app.network

import com.shoplite.app.network.model.AddCartRequest
import com.shoplite.app.network.model.CartResponse
import com.shoplite.app.network.model.LoginRequest
import com.shoplite.app.network.model.LoginResponse
import com.shoplite.app.network.model.Product
import com.shoplite.app.network.model.ProductsResponse
import com.shoplite.app.network.model.RefreshRequest
import com.shoplite.app.network.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun me(): UserProfile

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequest): Response<LoginResponse>

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ProductsResponse

    @GET("products/search")
    suspend fun search(@Query("q") query: String): ProductsResponse

    @GET("products/category-list")
    suspend fun getCategories(): List<String>

    @GET("products/category/{slug}")
    suspend fun getByCategory(@Path("slug") slug: String): Response<ProductsResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    @POST("carts/add")
    suspend fun addToCart(@Body body: AddCartRequest): Response<CartResponse>
}
