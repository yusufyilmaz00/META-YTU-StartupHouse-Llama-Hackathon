package com.balikllama.b1demo.repository

import com.balikllama.b1demo.model.auth.*

// Hata yönetimini kolaylaştırmak için Result wrapper kullanabiliriz
// ama şimdilik basit tutalım.

interface AuthRepository {
    suspend fun login(request: LoginRequest): LoginResponse
    suspend fun register(request: RegisterRequest): RegisterResponse
}