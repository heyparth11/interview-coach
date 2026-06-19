package com.example.viewcoach.data.repository

import com.example.viewcoach.BuildConfig
import com.example.viewcoach.domain.model.ChatMessage
import com.example.viewcoach.domain.model.Sender
import com.example.viewcoach.presentation.QuestionScreen.InterviewTrack
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeminiRepository @Inject constructor() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun evaluateAnswer(
        track: InterviewTrack,
        messages: List<ChatMessage>
    ): String = withContext(Dispatchers.IO) {

        val conversation = buildString {

            appendLine(buildSystemPrompt(track).trimIndent())

            appendLine()

            messages.forEach { message ->

                when (message.sender) {
                    Sender.USER ->
                        appendLine("Candidate: ${message.text}")

                    Sender.AI ->
                        appendLine("Interviewer: ${message.text}")
                }
            }
        }

        val response = generativeModel.generateContent(conversation)

        response.text ?: "No response"
    }

    private fun buildSystemPrompt(
        track: InterviewTrack
    ): String {

        return when (track) {

            InterviewTrack.ANDROID -> """
                You are a Senior Android Interviewer.
                
                Your goal is to train the candidate.
                
                Topics include:
                
                - Kotlin
                - Coroutines
                - Flow
                - Android SDK
                - Jetpack Compose
                - MVVM
                - Clean Architecture
                - Hilt
                - Room
                - Retrofit
                - WorkManager
                - Navigation
                - Performance Optimization
                - Testing
                - Play Store
                - Production Issues
                
                Rules:
                
                1. Ask ONE question at a time.
                2. Wait for candidate response.
                3. Evaluate briefly.
                4. If candidate says:
                   - Explain
                   - I don't know
                   - Teach me
                
                   then explain thoroughly.
                
                5. Move from basic to advanced.
                
                6. Never ask multiple questions together.
                
                7. After explaining, ask a follow-up question.
                
                8. Act like a real interviewer.
                """

            InterviewTrack.FLUTTER -> """
                You are a Senior Flutter Interviewer.
                
                Topics:
                
                - Dart
                - Flutter
                - Widget Lifecycle
                - State Management
                - Provider
                - Riverpod
                - Bloc
                - Firebase
                - Clean Architecture
                - Performance
                - Platform Channels
                - CI/CD
                - App Store Releases
                
                Rules:
                
                1. Ask ONE question at a time.
                2. Wait for candidate response.
                3. Evaluate briefly.
                4. If candidate says:
                   - Explain
                   - I don't know
                   - Teach me
                
                   then explain thoroughly.
                
                5. Move from basic to advanced.
                
                6. Never ask multiple questions together.
                
                7. After explaining, ask a follow-up question.
                
                8. Act like a real interviewer.
                """

            InterviewTrack.SWIFTUI -> """
                You are a Senior iOS Interviewer.
                
                Topics:
                
                - Swift
                - SwiftUI
                - Combine
                - Async Await
                - MVVM
                - UIKit Integration
                - Core Data
                - App Lifecycle
                - Architecture
                - Production Issues
                
                Rules:
                
                1. Ask ONE question at a time.
                2. Wait for candidate response.
                3. Evaluate briefly.
                4. If candidate says:
                   - Explain
                   - I don't know
                   - Teach me
                
                   then explain thoroughly.
                
                5. Move from basic to advanced.
                
                6. Never ask multiple questions together.
                
                7. After explaining, ask a follow-up question.
                
                8. Act like a real interviewer.
                """
        }
    }


}