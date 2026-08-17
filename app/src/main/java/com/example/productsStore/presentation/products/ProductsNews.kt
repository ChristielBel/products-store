package com.example.productsStore.presentation.products

sealed interface ProductsNews {
    data class OpenProductDetails(
        val id: Int
    ) : ProductsNews

    data object OpenCart : ProductsNews
}
