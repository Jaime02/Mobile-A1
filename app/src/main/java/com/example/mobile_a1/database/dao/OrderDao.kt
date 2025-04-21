package com.example.mobile_a1.database.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
        WHERE o.completed = 0
        GROUP BY o.id
        """
    )
    fun getPendingOrdersWithMetadata(): Flow<List<OrderWithMetadata>>

    @Query("SELECT * FROM Orders WHERE id = :orderId")
    fun get(orderId: Long): Flow<Order>

    @Insert
    fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Delete
    fun delete(order: Order)

    // Get all the orders that are not completed grouped into a map by supermarket name
    fun getPendingOrdersGroupedBySupermarket(): Flow<Map<String, List<OrderWithMetadata>>> {
        return getPendingOrdersWithMetadata().map { list ->
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
