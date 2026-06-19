package com.example.viewcoach.domain.model

data class ChatMessage(
    val id: Int,
    val text: String,
    val sender: Sender,
    val timestamp: String,
)

//System.currentTimeMillis(),