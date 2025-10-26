package com.balikllama.xpguiderdemo.repository

import com.google.gson.JsonObject

interface ChatbotRepository {
    /**
     * Analiz edilen zeka oranlarını backend'e gönderir.
     * @param ratios `trait_name` ve `score` içeren bir Map.
     * @return Başarılı ise [ApiResult.Success] içinde sunucunun [JsonObject] cevabını döner.
     *         Başarısız ise [ApiResult.Error] döner.
     */
    suspend fun sendRatiosToIntelligenceApi(ratios: Map<String, Double>): ApiResult<JsonObject>
    /**
     * API'den gelen ilk analiz mesajını döndürür.
     */
    fun getInitialAnalysisMessage(): String?

    /**
     * Mesaj kullanıldıktan sonra hafızadan temizler.
     */
    fun clearInitialAnalysisMessage()

    /**
     * Kullanıcının metin mesajını API'ye gönderir ve AI'dan yanıt alır.
     * @param message Gönderilecek metin.
     * @return Başarılı ise [ApiResult.Success] içinde AI'nın cevabını [String] olarak döner.
     */
    suspend fun sendMessage(message: String): ApiResult<String>
}