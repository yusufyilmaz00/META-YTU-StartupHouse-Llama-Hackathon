package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.model.auth.*

// Hata yönetimini kolaylaştırmak için Result wrapper kullanabiliriz
// ama şimdilik basit tutalım.

interface AuthRepository {
    /**
     * Kullanıcı girişi yapar.
     * @return Başarılı ise [ApiResult.Success] içinde [LoginResponse] döner.
     *         Başarısız ise [ApiResult.Error] içinde hata mesajı döner.
     */
    suspend fun login(request: LoginRequest): ApiResult<LoginResponse>

    /**
     * Yeni kullanıcı kaydı oluşturur.
     * @return Başarılı ise [ApiResult.Success] içinde [RegisterResponse] döner.
     *         Başarısız ise [ApiResult.Error] içinde hata mesajı döner.
     */
    suspend fun register(request: RegisterRequest): ApiResult<RegisterResponse>
}