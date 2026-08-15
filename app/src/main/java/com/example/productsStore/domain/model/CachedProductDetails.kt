package com.example.productsStore.domain.model

data class CachedProductDetails(
    val product: ProductDetails,
    val isStale: Boolean
)