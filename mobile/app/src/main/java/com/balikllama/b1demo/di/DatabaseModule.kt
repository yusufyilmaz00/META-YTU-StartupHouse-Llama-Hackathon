package com.balikllama.b1demo.di

import android.content.Context
import androidx.room.Room
import com.balikllama.b1demo.data.local.AppDatabase
import com.balikllama.b1demo.data.local.dao.InterestDao
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "b1_demo_database" // lokal db dosyasının adı
        ).build()
    }

    @Provides
    @Singleton
    fun provideInterestDao(appDatabase: AppDatabase): InterestDao {
        return appDatabase.interestDao()
    }
}
