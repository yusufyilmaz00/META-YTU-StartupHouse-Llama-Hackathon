package com.balikllama.xpguiderdemo.model.chat

import com.google.gson.annotations.SerializedName

// FastAPI'deki {"response": answer} buna karşılık gelir.
data class ChatResponse(
    @SerializedName("response")
    val response: String
)