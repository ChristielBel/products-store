package com.example.productsStore.data.remote

import com.example.productsStore.data.remote.dto.ProductDetailsDto
import com.example.productsStore.data.remote.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("select") select: String,
    ): ProductsResponseDto

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDetailsDto
}