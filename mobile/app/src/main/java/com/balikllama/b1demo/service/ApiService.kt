package com.balikllama.b1demo.service


import com.balikllama.b1demo.model.auth.LoginRequest
import com.balikllama.b1demo.model.auth.LoginResponse
import com.balikllama.b1demo.model.auth.RegisterRequest
import com.balikllama.b1demo.model.auth.RegisterResponse
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
}