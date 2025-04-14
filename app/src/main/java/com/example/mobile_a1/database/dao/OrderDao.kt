package com.example.mobile_a1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobile_a1.database.entities.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM Orders")
    fun getAll(): Flow<List<Order>>

    @Query("SELECT * FROM Orders WHERE id = :orderId")
    fun get(orderId: Int): Order

    @Insert
    fun insert(order: Order)

    @Delete
    fun delete(order: Order)

    @Query("""
    SELECT o.id AS orderId, p.name AS productName, ol.quantity AS quantity
    FROM Orders o
    INNER JOIN OrderLine ol ON o.id = ol.orderId
    INNER JOIN Products p ON p.id = ol.productId
    """)
    fun getOrdersWithProducts(): Flow<List<OrderWithProduct>>
}

data class OrderWithProduct(
    val orderId: Int,
    val productName: String,
    val quantity: Int
)
