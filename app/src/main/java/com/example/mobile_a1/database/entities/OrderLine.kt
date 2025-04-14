package com.example.mobile_a1.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "OrderLine",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["orderId"]), Index(value = ["productId"])]
)
data class OrderLine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int, // Foreign key to the Order entity
    val productId: Int, // Foreign key to the Product entity
    val quantity: Int // Quantity of this product in the order
)