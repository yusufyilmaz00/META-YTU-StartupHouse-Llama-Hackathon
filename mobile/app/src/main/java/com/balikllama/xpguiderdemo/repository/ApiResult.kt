package com.balikllama.xpguiderdemo.repository

/**
 * API çağrılarının sonucunu sarmalamak için kullanılan genel bir sınıf.
 * Bu sınıf, ya başarılı bir sonucu (Success) ya da bir hatayı (Error) temsil eder.
 */
sealed class ApiResult<out T> {
    /**
     * Başarılı bir API çağrısını temsil eder.
     * @param data API'den dönen veri.
     */
    data class Success<out T>(val data: T) : ApiResult<T>()

    /**
     * Başarısız bir API çağrısını temsil eder.
     * @param message Kullanıcıya gösterilebilecek anlaşılır hata mesajı.
     */
    data class Error(val message: String) : ApiResult<Nothing>()
}