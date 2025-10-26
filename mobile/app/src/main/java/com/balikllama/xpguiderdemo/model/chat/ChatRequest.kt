package com.balikllama.xpguiderdemo.model.chatbot

import com.google.gson.annotations.SerializedName

// FastAPI'deki user_input: str = Body(..., embed=True) buna karşılık gelir.
data class ChatRequest(
    @SerializedName("user_input")
    val userInput: String
)
