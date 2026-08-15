package com.example.productsStore.data.mapper

import com.example.productsStore.data.local.cart.CartEntity
import com.example.productsStore.domain.model.CartItem

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        productId = productId,
        title = title,
        price = price,
        thumbnail = thumbnail,
        quantity = quantity
    )
}