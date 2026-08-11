package com.example.productsStore.data.local.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun observeCart(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productId = :id")
    suspend fun getCartItem(id: Int): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE productId = :id")
    suspend fun increment(id: Int)

    @Query("DELETE FROM cart")
    suspend fun clear()
}