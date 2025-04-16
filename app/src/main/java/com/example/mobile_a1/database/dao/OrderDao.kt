package com.example.mobile_a1.database.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobile_a1.database.entities.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.TreeMap

@Dao
interface OrderDao {
    @Query("SELECT * FROM Orders")
    fun getAll(): Flow<List<Order>>

    @Query(
        """
        SELECT o.*, COUNT(ol.orderId) AS orderLineCount, s.name AS supermarketName
        FROM Orders o
        LEFT JOIN OrderLine ol ON o.id = ol.orderId
        LEFT JOIN Supermarkets s ON o.supermarketId = s.id
        GROUP BY o.id
        """
    )
    fun getAllWithMetadata(): Flow<List<OrderWithMetadata>>

    @Query("SELECT * FROM Orders WHERE id = :orderId")
    fun get(orderId: Int): Order

    @Insert
    fun insert(order: Order): Long

    @Delete
    fun delete(order: Order)

    fun getOrdersGroupedBySupermarket(): Flow<Map<String, List<OrderWithMetadata>>> {
        return getAllWithMetadata().map { list ->
            val map = TreeMap<String, MutableList<OrderWithMetadata>>()
            list.forEach { item ->
                if (!map.containsKey(item.supermarketName)) {
                    map[item.supermarketName] = mutableListOf(item)
                } else {
                    map[item.supermarketName]?.add(item)
                }
            }
            map
        }
    }
}

data class OrderWithMetadata(
    @Embedded val order: Order,
    @ColumnInfo(name = "orderLineCount") val orderLineCount: Int,
    @ColumnInfo(name = "supermarketName") val supermarketName: String
)
