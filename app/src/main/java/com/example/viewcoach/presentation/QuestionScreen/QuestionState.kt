package com.example.viewcoach.presentation.QuestionScreen

import com.example.viewcoach.domain.model.ChatMessage
import com.example.viewcoach.domain.model.Sender

data class InterviewUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            id = 1,
            text = "Tell me about yourself.",
            sender = Sender.AI,
            timestamp = System.currentTimeMillis().toString()
        )
    ),
    val inputText: String = "",
    val isLoading: Boolean = false
)