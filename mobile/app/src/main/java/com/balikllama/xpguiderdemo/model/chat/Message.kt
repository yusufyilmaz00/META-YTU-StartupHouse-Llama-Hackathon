package com.balikllama.xpguiderdemo.model.chat

/**
 * Chat ekranındaki tek bir mesajı temsil eder.
 * @param id Mesajın benzersiz kimliği (liste performansı için).
 * @param text Mesajın metin içeriği.
 * @param author Mesajın yazarı (AI veya USER).
 */
data class Message(
    val id: String,
    val text: String,
    val author: MessageAuthor
)