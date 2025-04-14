package com.example.mobile_a1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobile_a1.database.entities.OrderLine

@Dao
interface OrderLineDao {
    @Query("SELECT * FROM OrderLine")
    fun getAll(): List<OrderLine>

    @Query("SELECT * FROM OrderLine WHERE id = :orderLineId")
    fun get(orderLineId: Int): OrderLine

    @Insert
    fun insert(orderLine: OrderLine): Long

    @Delete
    fun delete(orderLine: OrderLine)
}