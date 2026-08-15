package com.example.productsStore.domain.repository

import com.example.productsStore.domain.model.CartItem
import com.example.productsStore.domain.model.ProductDetails
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartItem>>
    suspend fun addToCart(product: ProductDetails)
    suspend fun clearCart()
}