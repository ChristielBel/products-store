package com.example.productsStore.presentation.products

import ru.tinkoff.kotea.core.Next
import ru.tinkoff.kotea.core.Update

class ProductsUpdate : Update<
        ProductsState,
        ProductsEvent,
        ProductsCommand,
        ProductsNews
        > {

    override fun update(
        state: ProductsState,
        event: ProductsEvent,
    ): Next<
            ProductsState,
            ProductsCommand,
            ProductsNews
            > {

        return when (event) {
            is ProductsEvent.Ui.OnProductClicked ->
                Next(
                    state = state,
                    news = listOf(
                        ProductsNews.OpenProductDetails(event.id)
                    )
                )

            is ProductsEvent.Ui.OnCartClicked ->
                Next(
                    state = state,
                    news = listOf(
                        ProductsNews.OpenCart
                    )
                )

            is ProductsEvent.Internal.CartCountLoaded ->
                Next(
                    state = state.copy(
                        cartItemCount = event.count
                    )
                )
        }
    }
}
