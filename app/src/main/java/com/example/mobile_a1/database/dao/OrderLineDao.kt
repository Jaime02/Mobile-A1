package com.example.mobile_a1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_a1.database.entities.OrderLine
import com.example.mobile_a1.database.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderLineDao {
    @Query("SELECT * FROM OrderLine")
    fun getAll(): List<OrderLine>

    @Query("SELECT * FROM OrderLine WHERE id = :orderLineId")
    fun get(orderLineId: Int): OrderLine

    @Insert
    fun insert(orderLine: OrderLine): Long

    @Update
    suspend fun update(orderLine: OrderLine)

    @Delete
    fun delete(orderLine: OrderLine)

    @Query("SELECT ol.*, p.id AS product_id, p.name AS product_name FROM OrderLine ol INNER JOIN Products p ON ol.productId = p.id WHERE orderId = :orderId")    fun getOrderLinesWithProductsForOrder(orderId: Long): Flow<List<OrderLineWithProduct>>
}

data class OrderLineWithProduct(
    @Embedded val orderLine: OrderLine,
    @Embedded(prefix = "product_")
    val product: Product
)