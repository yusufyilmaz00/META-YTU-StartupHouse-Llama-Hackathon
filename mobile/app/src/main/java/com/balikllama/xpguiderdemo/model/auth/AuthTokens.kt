package com.balikllama.xpguiderdemo.model.auth

import com.google.gson.annotations.SerializedName

data class AuthTokens(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)
