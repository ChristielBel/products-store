package com.example.productsStore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsDto(
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