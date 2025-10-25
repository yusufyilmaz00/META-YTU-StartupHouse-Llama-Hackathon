package com.balikllama.xpguiderdemo.model.auth

// main.py'deki 'LoginIn' modeline karşılık gelir
data class LoginRequest(
    val email: String,
    val password: String
)