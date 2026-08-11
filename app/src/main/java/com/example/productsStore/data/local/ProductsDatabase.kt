package com.example.productsStore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productsStore.data.local.cart.CartDao
import com.example.productsStore.data.local.cart.CartEntity
import com.example.productsStore.data.local.product.ProductDetailsEntity
import com.example.productsStore.data.local.product.ProductsDao

@Database(
    entities = [
        ProductDetailsEntity::class,
        CartEntity::class],
    version = 3,
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
    abstract fun cartDao(): CartDao
}