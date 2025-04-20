package com.example.mobile_a1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobile_a1.database.entities.Supermarket
import kotlinx.coroutines.flow.Flow

@Dao
interface SupermarketDao {
    @Query("SELECT * FROM Supermarkets")
    fun getAll(): List<Supermarket>

    @Query("SELECT * FROM Supermarkets WHERE id = :supermarketId")
    fun get(supermarketId: Long): Flow<Supermarket>

    @Insert
    fun insert(supermarket: Supermarket): Long

    @Delete
    fun delete(supermarket: Supermarket)
}