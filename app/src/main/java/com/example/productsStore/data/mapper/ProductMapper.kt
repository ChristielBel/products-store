package com.example.productsStore.data.mapper

import com.example.productsStore.data.remote.dto.ProductDto
import com.example.productsStore.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        brand = brand
    )
}
