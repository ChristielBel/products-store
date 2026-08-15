package com.example.productsStore.domain.repository

import com.example.productsStore.domain.model.CachedProductDetails
import com.example.productsStore.domain.model.Product

interface ProductsRepository {

    suspend fun getProducts(
        limit: Int,
        skip: Int
    ): List<Product>

    suspend fun getProductDetails(id: Int): CachedProductDetails
}