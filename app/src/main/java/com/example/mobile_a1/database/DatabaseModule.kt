package com.example.mobile_a1.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.ProductDao
import com.example.mobile_a1.database.entities.Order
import com.example.mobile_a1.database.entities.OrderLine
import com.example.mobile_a1.database.entities.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Singleton
import android.util.Log

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private var INSTANCE: AppDatabase? = null

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        seedDb(database)
                    }
                }
            }
        }

        val db = Room.databaseBuilder(
            appContext, AppDatabase::class.java, "A1DB"
        ).fallbackToDestructiveMigration(true).addCallback(callback).build()

        INSTANCE = db
        return db
    }

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderLineDao(db: AppDatabase): OrderLineDao = db.orderLineDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    fun seedDb(database: AppDatabase) {
        val orderDao = database.orderDao()
        val productDao = database.productDao()
        val orderLineDao = database.orderLineDao()

        val product1Id = productDao.insert(Product(name = "Ciruelas"))
        val product2Id = productDao.insert(Product(name = "Palillos de dientes"))
        val product3Id = productDao.insert(Product(name = "Cerveza"))
        val product4Id = productDao.insert(Product(name = "Alubias rojas"))
        val product5Id = productDao.insert(Product(name = "Piparras"))
        val product6Id = productDao.insert(Product(name = "Aceitunas"))

        val order1Id = orderDao.insert(Order(orderDate = Date()))

        val order1Line1 = OrderLine(orderId = order1Id, productId = product1Id, quantity = 2)
        val order1Line2 = OrderLine(orderId = order1Id, productId = product2Id, quantity = 3)
        val order1Line3 = OrderLine(orderId = order1Id, productId = product3Id, quantity = 4)
        val order1Line4 = OrderLine(orderId = order1Id, productId = product4Id, quantity = 5)

        orderLineDao.insert(order1Line1)
        orderLineDao.insert(order1Line2)
        orderLineDao.insert(order1Line3)
        orderLineDao.insert(order1Line4)

        Log.d("DatabaseModule", "order line: $order1Line1")
    }
}
