package com.example.productsStore.data.repository

import com.example.productsStore.data.local.cart.CartDao
import com.example.productsStore.data.local.cart.CartEntity
import com.example.productsStore.data.mapper.toDomain
import com.example.productsStore.domain.model.CartItem
import com.example.productsStore.domain.model.ProductDetails
import com.example.productsStore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val dao: CartDao
) : CartRepository {

    override fun observeCart(): Flow<List<CartItem>> {
        return dao.observeCart().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(product: ProductDetails) {
        val exists = dao.getCartItem(product.id)

        if (exists != null) {
            dao.increment(product.id)
        } else {
            dao.insert(
                CartEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    thumbnail = product.thumbnail,
                    quantity = 1,
                )
            )
        }
    }

    override suspend fun clearCart() {
        dao.clear()
    }
}