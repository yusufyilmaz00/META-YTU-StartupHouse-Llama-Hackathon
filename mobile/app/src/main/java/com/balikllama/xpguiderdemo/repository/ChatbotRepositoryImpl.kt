package com.balikllama.xpguiderdemo.repository

import android.util.Log
import com.balikllama.xpguiderdemo.model.chatbot.ChatRequest
import com.balikllama.xpguiderdemo.service.ApiService
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ChatbotRepository {
    private var initialMessage: String? = null

    override suspend fun sendRatiosToIntelligenceApi(ratios: Map<String, Double>): ApiResult<JsonObject> {
        return withContext(Dispatchers.IO) {
            try {
                // İsteği FastAPI'nin beklediği formata getiriyoruz: {"ratios": {...}}
                // Ancak FastAPI kodunuz direkt olarak dictionary bekliyor, bu yüzden wrapper'a gerek yok.
                val response = apiService.sendIntelligenceData(ratios)

                if (response.isSuccessful && response.body() != null) {
                    val jsonBody = response.body()!!
                    Log.d("ChatbotRepository", "API Success: $jsonBody")

                    if (jsonBody.has("response")) {
                        // Varsa, içindeki metni al ve initialMessage değişkenine ata.
                        initialMessage = jsonBody.get("response").asString
                        Log.d(
                            "ChatbotRepository",
                            "İlk analiz mesajı başarıyla yakalandı ve kaydedildi."
                        )
                    } else {
                        Log.w("ChatbotRepository", "API cevabında 'response' anahtarı bulunamadı.")
                    }

                    ApiResult.Success(jsonBody)
                } else {
                    initialMessage = null
                    val errorBody = response.errorBody()?.string() ?: "Bilinmeyen sunucu hatası"
                    Log.e("ChatbotRepository", "API Error: ${response.code()} - $errorBody")
                    ApiResult.Error("Sunucudan hata alındı: ${response.code()}")
                }
            } catch (e: Exception) {
                // Herhangi bir hata durumunda mesajı temizle.
                initialMessage = null
                Log.e("ChatbotRepository", "Unknown Error in sendRatios: ${e.message}", e)
                // Hata türüne göre loglama burada daha detaylı yapılabilir.
                when (e) {
                    is IOException -> ApiResult.Error("İnternet bağlantısı kurulamadı.")
                    is HttpException -> ApiResult.Error("HTTP Hatası: ${e.code()}")
                    else -> ApiResult.Error("Beklenmedik bir hata oluştu.")
                }
            }
        }
    }

    override fun getInitialAnalysisMessage(): String? {
        return initialMessage
    }

    override fun clearInitialAnalysisMessage() {
        initialMessage = null
    }

    override suspend fun sendMessage(message: String): ApiResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. İsteği oluştur
                val request = ChatRequest(userInput = message)
                // 2. ApiService üzerinden endpoint'i çağır
                val response = apiService.postChatMessage(request)
                // 3. Başarılı yanıtı sarmala ve döndür
                Log.d("ChatbotRepository", "Mesaj başarıyla gönderildi, AI yanıtı: ${response.response}")
                ApiResult.Success(response.response)
            } catch (e: Exception) {
                // 4. Hata durumunda hatayı logla ve sarmala
                Log.e("ChatbotRepository", "sendMessage Hata: ${e.message}", e)
                ApiResult.Error("Mesaj gönderilirken bir hata oluştu.")
            }
        }
    }
}