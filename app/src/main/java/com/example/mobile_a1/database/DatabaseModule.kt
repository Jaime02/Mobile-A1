package com.example.mobile_a1.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.ProductDao
import com.example.mobile_a1.database.dao.SupermarketDao
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
import javax.inject.Singleton
import com.example.mobile_a1.database.entities.Supermarket
import java.util.Calendar

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

    @Provides
    fun provideSupermarketDao(db: AppDatabase): SupermarketDao = db.supermarketDao()

    fun seedDb(database: AppDatabase) {
        val orderDao = database.orderDao()
        val productDao = database.productDao()
        val orderLineDao = database.orderLineDao()
        val supermarketDao = database.supermarketDao()

        val mercadona = Supermarket(name = "Mercadona")
        val carrefour = Supermarket(name = "Carrefour")
        val eroski = Supermarket(name = "Eroski")
        val alcampo = Supermarket(name = "Alcampo")
        val aldi = Supermarket(name = "Aldi")

        val mercadonaId = supermarketDao.insert(mercadona)
        val carrefourId = supermarketDao.insert(carrefour)
        val eroskiId = supermarketDao.insert(eroski)
        val alcampoId = supermarketDao.insert(alcampo)
        supermarketDao.insert(aldi)

        val product1Id = productDao.insert(Product(name = "Ciruelas"))
        val product2Id = productDao.insert(Product(name = "Palillos de dientes"))
        val product3Id = productDao.insert(Product(name = "Cerveza"))
        val product4Id = productDao.insert(Product(name = "Alubias rojas"))
        val product5Id = productDao.insert(Product(name = "Piparras"))
        val product6Id = productDao.insert(Product(name = "Aceitunas"))

        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val dayAfterTomorrow = calendar.time
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val weekAfterTomorrow = calendar.time

        val order1Id = orderDao.insert(Order(orderDate = today, supermarketId = mercadonaId))
        val order2Id = orderDao.insert(Order(orderDate = tomorrow, supermarketId = carrefourId))
        val order3Id = orderDao.insert(Order(orderDate = dayAfterTomorrow, supermarketId = eroskiId))
        val order4Id = orderDao.insert(Order(orderDate = dayAfterTomorrow, supermarketId = alcampoId))
        val order5Id = orderDao.insert(Order(orderDate = weekAfterTomorrow, supermarketId = mercadonaId))

        val order1Line1 = OrderLine(orderId = order1Id, productId = product1Id, quantity = 2)
        val order1Line2 = OrderLine(orderId = order1Id, productId = product2Id, quantity = 3)
        val order1Line3 = OrderLine(orderId = order1Id, productId = product3Id, quantity = 4)
        val order1Line4 = OrderLine(orderId = order1Id, productId = product4Id, quantity = 5)
        val order2Line1 = OrderLine(orderId = order2Id, productId = product5Id, quantity = 1)
        val order2Line2 = OrderLine(orderId = order2Id, productId = product6Id, quantity = 3)
        val order3Line1 = OrderLine(orderId = order3Id, productId = product1Id, quantity = 2)
        val order3Line2 = OrderLine(orderId = order3Id, productId = product2Id, quantity = 2)
        val order4Line1 = OrderLine(orderId = order4Id, productId = product3Id, quantity = 4)
        val order4Line2 = OrderLine(orderId = order4Id, productId = product4Id, quantity = 5)
        val order4Line3 = OrderLine(orderId = order4Id, productId = product5Id, quantity = 1)
        val order5Line1 = OrderLine(orderId = order5Id, productId = product6Id, quantity = 3)
        val order5Line2 = OrderLine(orderId = order5Id, productId = product1Id, quantity = 2)
        val order5Line3 = OrderLine(orderId = order5Id, productId = product2Id, quantity = 1)

        orderLineDao.insert(order1Line1)
        orderLineDao.insert(order1Line2)
        orderLineDao.insert(order1Line3)
        orderLineDao.insert(order1Line4)
        orderLineDao.insert(order2Line1)
        orderLineDao.insert(order2Line2)
        orderLineDao.insert(order3Line1)
        orderLineDao.insert(order3Line2)
        orderLineDao.insert(order4Line1)
        orderLineDao.insert(order4Line2)
        orderLineDao.insert(order4Line3)
        orderLineDao.insert(order5Line1)
        orderLineDao.insert(order5Line2)
        orderLineDao.insert(order5Line3)
    }
}
