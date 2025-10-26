package com.balikllama.xpguiderdemo.service


import com.balikllama.xpguiderdemo.model.auth.LoginRequest
import com.balikllama.xpguiderdemo.model.auth.LoginResponse
import com.balikllama.xpguiderdemo.model.auth.RegisterRequest
import com.balikllama.xpguiderdemo.model.auth.RegisterResponse
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // main.py -> @app.post("/auth/register")
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    // main.py -> @app.post("/auth/login")
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // main.py -> @app.get("/me")
    // TODO: Token'ı kaydettikten sonra burayı kullanacağız.
    // @GET("me")
    // suspend fun getMyProfile(
    //    @Header("Authorization") token: String
    // )

    /**
     * Zeka oranlarını ve kullanıcı mesajını chatbot'a gönderir.
     * @param requestBody `{"ratios": {...}, "message": "..."}` formatında bir JSON.
     * @return Sunucudan gelen cevabı (bu durumda bir JSON objesi) döner.
     */
    @POST("intelligence")
    suspend fun sendIntelligenceData(
        @Body requestBody: Map<String, @JvmSuppressWildcards Any>
    ): Response<JsonObject>
}