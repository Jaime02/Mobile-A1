package com.example.mobile_a1.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderDate: Date = Date()
)
