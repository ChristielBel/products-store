package com.example.productsStore.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.productsStore.domain.repository.CartRepository
import com.example.productsStore.domain.repository.ProductsRepository
import com.example.productsStore.presentation.paging.ProductsPagingSource
import ru.tinkoff.kotea.core.KoteaStore

class ProductsViewModel(
    productsRepository: ProductsRepository,
    cartRepository: CartRepository,
) : ViewModel() {
    private companion object {
        const val PAGE_SIZE = 20
        const val INITIAL_LOAD_SIZE = PAGE_SIZE
        const val PREFETCH_DISTANCE = PAGE_SIZE / 4
    }

    val products =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                ProductsPagingSource(productsRepository)
            }
        ).flow.cachedIn(viewModelScope)

    val store =
        KoteaStore<
                ProductsState,
                ProductsEvent,
                ProductsEvent.Ui,
                ProductsCommand,
                ProductsNews
                >(
            initialState = ProductsState(),
            initialCommands = listOf(
                ProductsCommand.ObserveCartCount
            ),
            commandsFlowHandlers = listOf(
                ProductsCommandHandler(cartRepository)
            ),
            update = ProductsUpdate()
        )

    init {
        store.launchIn(viewModelScope)
    }
}
