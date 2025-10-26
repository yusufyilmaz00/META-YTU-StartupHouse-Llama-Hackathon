package com.balikllama.xpguiderdemo.model.auth

import com.google.gson.annotations.SerializedName

// main.py'deki 'RegisterIn' modeline karşılık gelir
data class RegisterRequest (
    val email: String,
    val password: String,
    val metadata: Metadata
)

data class Metadata(
    @SerializedName("name")
    val name: String,

    @SerializedName("setupCompleted")
    val setupCompleted: Boolean? = false
)