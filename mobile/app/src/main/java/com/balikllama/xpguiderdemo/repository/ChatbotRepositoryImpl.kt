package com.balikllama.xpguiderdemo.repository

import android.util.Log
import com.balikllama.xpguiderdemo.service.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ChatbotRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ChatbotRepository {

    override suspend fun sendRatiosToIntelligenceApi(ratios: Map<String, Double>): ApiResult<JsonObject> {
        return withContext(Dispatchers.IO) {
            try {
                // İsteği FastAPI'nin beklediği formata getiriyoruz: {"ratios": {...}}
                // Ancak FastAPI kodunuz direkt olarak dictionary bekliyor, bu yüzden wrapper'a gerek yok.
                val response = apiService.sendIntelligenceData(ratios)

                if (response.isSuccessful && response.body() != null) {
                    // Logcat'e yazdırma işlemi burada yapılıyor.
                    Log.d("ChatbotRepository", "API Success: ${response.body().toString()}")
                    ApiResult.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Bilinmeyen sunucu hatası"
                    Log.e("ChatbotRepository", "API Error: ${response.code()} - $errorBody")
                    ApiResult.Error("Sunucudan hata alındı: ${response.code()}")
                }
            } catch (e: IOException) {
                Log.e("ChatbotRepository", "Network Error: ${e.message}", e)
                ApiResult.Error("İnternet bağlantısı kurulamadı.")
            } catch (e: HttpException) {
                Log.e("ChatbotRepository", "HTTP Error: ${e.code()} - ${e.message()}", e)
                ApiResult.Error("HTTP Hatası: ${e.code()}")
            } catch (e: Exception) {
                Log.e("ChatbotRepository", "Unknown Error: ${e.message}", e)
                ApiResult.Error("Beklenmedik bir hata oluştu.")
            }
        }
    }
}