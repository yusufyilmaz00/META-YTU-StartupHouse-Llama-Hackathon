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
}