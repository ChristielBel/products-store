package com.example.productsStore.data.mapper

import com.example.productsStore.data.local.product.ProductDetailsEntity
import com.example.productsStore.data.remote.dto.ProductDetailsDto
import com.example.productsStore.domain.model.ProductDetails

fun ProductDetailsDto.toEntity(
    cachedAt: Long
): ProductDetailsEntity {
    return ProductDetailsEntity(
        id = id,
        title = title,
        description = description,
        rating = rating,
        price = price,
        weight = weight,
        availabilityStatus = availabilityStatus,
        warrantyInformation = warrantyInformation,
        cachedAt = cachedAt,
        thumbnail = thumbnail,
    )
}

fun ProductDetailsDto.toDomain(): ProductDetails {
    return ProductDetails(
        id = id,
        title = title,
        description = description,
        rating = rating,
        price = price,
        weight = weight,
        availabilityStatus = availabilityStatus,
        warrantyInformation = warrantyInformation,
        thumbnail = thumbnail
    )
}

fun ProductDetailsEntity.toDomain(): ProductDetails {
    return ProductDetails(
        id = id,
        title = title,
        description = description,
        rating = rating,
        price = price,
        weight = weight,
        availabilityStatus = availabilityStatus,
        warrantyInformation = warrantyInformation,
        thumbnail = thumbnail
    )
}