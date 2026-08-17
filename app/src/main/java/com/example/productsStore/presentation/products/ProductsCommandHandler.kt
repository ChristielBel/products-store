package com.example.productsStore.presentation.products

import com.example.productsStore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.tinkoff.kotea.core.CommandsFlowHandler

class ProductsCommandHandler(
    private val cartRepository: CartRepository,
) : CommandsFlowHandler<
        ProductsCommand,
        ProductsEvent
        > {

    override fun handle(
        commands: Flow<ProductsCommand>
    ): Flow<ProductsEvent> = channelFlow {
        commands.collect { command ->
            when (command) {
                ProductsCommand.ObserveCartCount -> {
                    launch {
                        cartRepository.observeCart()
                            .collectLatest { items ->
                                send(
                                    ProductsEvent.Internal.CartCountLoaded(
                                        items.sumOf { it.quantity }
                                    )
                                )
                            }
                    }
                }
            }
        }
    }
}
