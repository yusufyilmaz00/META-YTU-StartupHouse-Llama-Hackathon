package com.balikllama.xpguiderdemo.model.auth

// main.py'deki 'RegisterIn' modeline karşılık gelir
data class RegisterRequest (
    val email: String,
    val password: String,
    val metadata: Metadata
)

data class Metadata(
    val name: String
)