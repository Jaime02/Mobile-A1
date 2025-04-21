package com.example.mobile_a1.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity class representing a Supermarket in the database.
@Entity(tableName = "Supermarkets")
data class Supermarket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)