package com.balikllama.xpguiderdemo.di

import android.content.Context
import androidx.room.Room
import com.balikllama.xpguiderdemo.data.local.AppDatabase
import com.balikllama.xpguiderdemo.data.local.dao.CalculationFactorDao
import com.balikllama.xpguiderdemo.data.local.dao.InterestDao
import com.balikllama.xpguiderdemo.data.local.dao.TraitDao
import com.balikllama.xpguiderdemo.data.local.dao.QuestionDao
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
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideInterestDao(appDatabase: AppDatabase): InterestDao {
        return appDatabase.interestDao()
    }

    @Provides
    @Singleton
    fun provideTraitDao(appDatabase: AppDatabase): TraitDao { // YENİ
        return appDatabase.traitDao()
    }

    @Provides
    @Singleton
    fun provideQuestionDao(appDatabase: AppDatabase): QuestionDao { // YENİ FONKSİYON
        return appDatabase.questionDao()
    }

    @Provides
    @Singleton
    fun provideCalculationFactorDao(appDatabase: AppDatabase): CalculationFactorDao { // YENİ FONKSİYON
        return appDatabase.calculationFactorDao()
    }

}
