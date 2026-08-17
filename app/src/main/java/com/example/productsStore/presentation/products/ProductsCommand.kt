package com.example.productsStore.presentation.products

sealed interface ProductsCommand {
    data object ObserveCartCount : ProductsCommand
}
