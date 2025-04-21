package com.example.mobile_a1.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity class representing a Product in the database.
@Entity(tableName = "Products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)