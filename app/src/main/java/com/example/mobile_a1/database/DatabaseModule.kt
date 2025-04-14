package com.example.mobile_a1.database

import android.content.Context
import androidx.room.Room
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "A1DB"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderLineDao(db: AppDatabase): OrderLineDao = db.orderLineDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
}
