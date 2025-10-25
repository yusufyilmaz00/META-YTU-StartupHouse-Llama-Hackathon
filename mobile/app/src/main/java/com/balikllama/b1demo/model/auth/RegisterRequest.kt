package com.balikllama.b1demo.model.auth

// main.py'deki 'RegisterIn' modeline karşılık gelir
data class RegisterRequest (
    val email: String,
    val password: String
)