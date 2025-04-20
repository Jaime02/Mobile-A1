package com.example.mobile_a1.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Orders",
    foreignKeys = [
        ForeignKey(
            entity = Supermarket::class,
            parentColumns = ["id"],
            childColumns = ["supermarketId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index("supermarketId")]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderDate: Date = Date(),
    val completed: Boolean = false,
    val supermarketId: Long,
)
