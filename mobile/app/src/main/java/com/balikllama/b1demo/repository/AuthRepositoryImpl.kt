package com.balikllama.b1demo.repository


import com.balikllama.b1demo.model.auth.*
import com.balikllama.b1demo.service.ApiService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): LoginResponse {
        // ViewModel'e ham hatayı (örn: HttpException) iletmek
        // için try-catch'i ViewModel'de yapacağız.
        return apiService.login(request)
    }

    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }
}