package com.example.productsStore.domain.model

data class ProductDetails(
    val id: Int,
    val title: String,
    val description: String?,
    val rating: Double,
    val price: Double,
    val weight: Double?,
    val availabilityStatus: String?,
    val warrantyInformation: String?,
    val thumbnail: String?,
)