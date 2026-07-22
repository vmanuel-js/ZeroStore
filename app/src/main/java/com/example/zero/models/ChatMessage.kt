package com.example.zero.models

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val texto: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
