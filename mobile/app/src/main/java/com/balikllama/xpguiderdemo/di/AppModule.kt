package com.balikllama.xpguiderdemo.di

import android.content.Context
import androidx.room.Room
import com.balikllama.xpguiderdemo.data.local.AppDatabase
import com.balikllama.xpguiderdemo.data.local.dao.TestResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}