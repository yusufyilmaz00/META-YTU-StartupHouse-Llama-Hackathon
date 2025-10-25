package com.balikllama.xpguiderdemo.model.auth

import com.google.gson.annotations.SerializedName

// main.py'deki 'RegisterOut' modeline karşılık gelir
data class RegisterResponse(
    @SerializedName("user_id")
    val userId: String,
    val email: String,
    @SerializedName("requires_email_verification")
    val requiresEmailVerification: Boolean
)