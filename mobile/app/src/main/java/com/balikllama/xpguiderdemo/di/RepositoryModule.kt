package com.balikllama.xpguiderdemo.di


import com.balikllama.xpguiderdemo.repository.AuthRepository
import com.balikllama.xpguiderdemo.repository.AuthRepositoryImpl
import com.balikllama.xpguiderdemo.repository.TestRepository
import com.balikllama.xpguiderdemo.repository.TestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository
}