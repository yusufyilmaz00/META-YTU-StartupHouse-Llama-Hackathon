package com.balikllama.xpguiderdemo.repository


import com.balikllama.xpguiderdemo.model.auth.LoginRequest
import com.balikllama.xpguiderdemo.model.auth.LoginResponse
import com.balikllama.xpguiderdemo.model.auth.RegisterRequest
import com.balikllama.xpguiderdemo.model.auth.RegisterResponse
import com.balikllama.xpguiderdemo.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): ApiResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(request)
                ApiResult.Success(response)
            } catch (e: IOException) {
                ApiResult.Error("İnternet bağlantısı kurulamadı. Lütfen ağ ayarlarınızı kontrol edin.")
            } catch (e: HttpException) { // <-- DOĞRU SINIFI YAKALA
                // Sunucudan 4xx veya 5xx gibi bir HTTP hata kodu döndü.
                // FastAPI sunucunun döndüğü kodları burada yönetebiliriz.
                when (e.code()) {
                    401, 403 -> ApiResult.Error("E-posta veya şifre hatalı.")
                    404 -> ApiResult.Error("Ulaşmaya çalıştığınız adres sunucuda bulunamadı.")
                    in 500..599 -> ApiResult.Error("Sunucuda bir sorun oluştu. Lütfen daha sonra tekrar deneyin.")
                    else -> ApiResult.Error("Bir hata oluştu. Hata Kodu: ${e.code()}")
                }
            } catch (e: Exception) {
                // JSON parse hatası gibi beklenmedik diğer tüm hatalar
                ApiResult.Error("Beklenmedik bir hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    override suspend fun register(request: RegisterRequest): ApiResult<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(request)
                ApiResult.Success(response)
            } catch (e: IOException) {
                ApiResult.Error("İnternet bağlantısı kurulamadı. Lütfen ağ ayarlarınızı kontrol edin.")
            } catch (e: HttpException) { // <-- DOĞRU SINIFI YAKALA
                // FastAPI'den dönen HTTP hata kodlarına göre özel mesajlar
                when (e.code()) {
                    400 -> ApiResult.Error("Girilen bilgiler eksik veya hatalı.")
                    // FastAPI'de "user already exists" için 409 Conflict de kullanılabilir.
                    409 -> ApiResult.Error("Bu e-posta adresi zaten kullanılıyor.")
                    else -> ApiResult.Error("Bir hata oluştu. Hata Kodu: ${e.code()}")
                }
            } catch (e: Exception) {
                ApiResult.Error("Beklenmedik bir hata oluştu: ${e.localizedMessage}")
            }
        }
    }
}