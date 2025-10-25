package com.balikllama.b1demo.model.auth

import com.google.gson.annotations.SerializedName

// main.py'deki 'LoginOut' modeline karşılık gelir
data class LoginResponse (
    @SerializedName("user_id")
    val userId: String,
    val email: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)