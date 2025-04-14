package com.example.mobile_a1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobile_a1.database.entities.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Products")
    fun getAll(): List<Product>

    @Query("SELECT * FROM Products WHERE id = :productId")
    fun get(productId: Int): Product

    @Insert
    fun insert(product: Product): Long

    @Delete
    fun delete(product: Product)
}