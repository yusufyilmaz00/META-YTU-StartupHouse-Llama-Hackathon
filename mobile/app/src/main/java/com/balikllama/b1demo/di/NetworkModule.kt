package com.balikllama.b1demo.di


import com.balikllama.b1demo.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Render backend url
    private const val BASE_URL = "https://meta-fastapi-backend.onrender.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        // Build tipine göre log seviyesini dinamik olarak ayarla
        val level = if (BuildConfig.DEBUG) {
            // Debug build'de tüm body'yi logla
            HttpLoggingInterceptor.Level.BODY
        } else {
            // Release build'de logları kapat
            HttpLoggingInterceptor.Level.NONE
        }
        return HttpLoggingInterceptor().setLevel(level)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // YENİ EKLENEN ZAMAN AŞIMLARI
            .connectTimeout(30, TimeUnit.SECONDS) // Sunucuya bağlanma zaman aşımı
            .readTimeout(30, TimeUnit.SECONDS)    // Sunucudan veri okuma zaman aşımı
            .writeTimeout(30, TimeUnit.SECONDS)   // Sunucuya veri yazma zaman aşımı
            .retryOnConnectionFailure(true)     // Bağlantı hatası durumunda yeniden denemeyi etkinleştir
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}