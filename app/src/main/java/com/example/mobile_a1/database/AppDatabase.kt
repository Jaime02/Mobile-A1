package com.example.mobile_a1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.ProductDao
import com.example.mobile_a1.database.dao.SupermarketDao
import com.example.mobile_a1.database.entities.Order
import com.example.mobile_a1.database.entities.OrderLine
import com.example.mobile_a1.database.entities.Product
import com.example.mobile_a1.database.entities.Supermarket

// Module that provides the Room database and its DAOs.
@Database(
    entities = [Order::class, OrderLine::class, Product::class, Supermarket::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun orderLineDao(): OrderLineDao
    abstract fun productDao(): ProductDao
    abstract fun supermarketDao(): SupermarketDao
}
