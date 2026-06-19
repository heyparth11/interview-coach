package com.example.viewcoach.presentation.QuestionScreen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viewcoach.data.repository.GeminiRepository
import com.example.viewcoach.domain.model.ChatMessage
import com.example.viewcoach.domain.model.Sender
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

@HiltViewModel
class InterviewViewModel
@Inject
constructor(
        private val repository: GeminiRepository,
        savedStateHandle: SavedStateHandle,
        @ApplicationContext context: Context
) : ViewModel() {

    private fun getCurrentFormattedTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private val track = InterviewTrack.valueOf(savedStateHandle["track"]!!)

    private val interviewerRole =
            when (track) {
                InterviewTrack.ANDROID -> "Senior Android Interviewer"
                InterviewTrack.FLUTTER -> "Senior Flutter Interviewer"
                InterviewTrack.SWIFTUI -> "Senior iOS Interviewer"
            }

    private val chatFile = File(context.filesDir, "chat_${track.name}.json")

    var uiState by mutableStateOf(InterviewUiState(messages = emptyList()))
        private set

    init {
        loadMessages()
    }

    private fun loadMessages() {
        val loaded =
                try {
                    if (chatFile.exists()) {
                        val jsonStr = chatFile.readText()
                        val jsonArray = JSONArray(jsonStr)
                        val list = mutableListOf<ChatMessage>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObj = jsonArray.getJSONObject(i)
                            list.add(
                                    ChatMessage(
                                            id = jsonObj.getInt("id"),
                                            text = jsonObj.getString("text"),
                                            sender = Sender.valueOf(jsonObj.getString("sender")),
                                            timestamp = jsonObj.getString("timestamp")
                                    )
                            )
                        }
                        list
                    } else {
                        emptyList()
                    }
                } catch (e: Exception) {
                    emptyList()
                }

        if (loaded.isNotEmpty()) {
            uiState = uiState.copy(messages = loaded)
        } else {
            val defaultGreeting =
                    listOf(
                            ChatMessage(
                                    id = 1,
                                    sender = Sender.AI,
                                    timestamp = getCurrentFormattedTime(),
                                    text =
                                            """
                    Welcome to your mock interview.
                    
                    I'll act as a $interviewerRole.
                    
                    Let's begin.
                    
                    Tell me about yourself and your experience as a mobile developer.
                    """.trimIndent()
                            )
                    )
            uiState = uiState.copy(messages = defaultGreeting)
            saveMessages(defaultGreeting)
        }
    }

    private fun saveMessages(messages: List<ChatMessage>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonArray = JSONArray()
                messages.forEach { msg ->
                    val jsonObj = JSONObject()
                    jsonObj.put("id", msg.id)
                    jsonObj.put("text", msg.text)
                    jsonObj.put("sender", msg.sender.name)
                    jsonObj.put("timestamp", msg.timestamp)
                    jsonArray.put(jsonObj)
                }
                chatFile.writeText(jsonArray.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            try {
                if (chatFile.exists()) {
                    chatFile.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val defaultGreeting =
                    listOf(
                            ChatMessage(
                                    id = 1,
                                    sender = Sender.AI,
                                    timestamp = getCurrentFormattedTime(),
                                    text =
                                            """
                    Welcome to your mock interview.
                    
                    I'll act as a $interviewerRole.
                    
                    Let's begin.
                    
                    Tell me about yourself and your experience as a mobile developer.
                    """.trimIndent()
                            )
                    )
            uiState = uiState.copy(messages = defaultGreeting, inputText = "", isLoading = false)
        }
    }

    fun onInputChanged(text: String) {
        uiState = uiState.copy(inputText = text)
    }

    fun sendMessage() {

        val userInput = uiState.inputText.trim()

        if (userInput.isBlank()) return

        val userMessage =
                ChatMessage(
                        id = uiState.messages.size + 1,
                        text = userInput,
                        sender = Sender.USER,
                        timestamp = getCurrentFormattedTime()
                )

        uiState =
                uiState.copy(
                        messages = uiState.messages + userMessage,
                        inputText = "",
                        isLoading = true
                )
        saveMessages(uiState.messages)

        viewModelScope.launch {
            try {

                val aiReply = repository.evaluateAnswer(track, uiState.messages)

                val aiMessage =
                        ChatMessage(
                                id = uiState.messages.size + 1,
                                text = aiReply,
                                sender = Sender.AI,
                                timestamp = getCurrentFormattedTime()
                        )

                uiState = uiState.copy(messages = uiState.messages + aiMessage, isLoading = false)
                saveMessages(uiState.messages)
            } catch (e: Exception) {

                val errorMessage =
                        ChatMessage(
                                id = uiState.messages.size + 1,
                                text = "Something went wrong: ${e.message}",
                                sender = Sender.AI,
                                timestamp = getCurrentFormattedTime()
                        )

                uiState =
                        uiState.copy(messages = uiState.messages + errorMessage, isLoading = false)
                saveMessages(uiState.messages)
            }
        }
    }
}
