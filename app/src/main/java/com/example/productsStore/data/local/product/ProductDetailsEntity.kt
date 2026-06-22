package com.example.productsStore.data.local.product

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_details")
data class ProductDetailsEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String?,
    val rating: Double,
    val price: Double,
    val weight: Double?,
    val availabilityStatus: String?,
    val warrantyInformation: String?,
    val cachedAt: Long,
    val thumbnail: String?,
)