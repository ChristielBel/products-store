package com.example.productsStore.presentation.products

sealed interface ProductsEvent {
    sealed interface Ui : ProductsEvent {
        data class OnProductClicked(
            val id: Int
        ) : Ui

        data object OnCartClicked : Ui
    }

    sealed interface Internal : ProductsEvent {
        data class CartCountLoaded(
            val count: Int
        ) : Internal
    }
}
