package com.example.viewcoach.presentation.QuestionScreen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.viewcoach.AppTheme
import com.example.viewcoach.domain.model.ChatMessage
import com.example.viewcoach.domain.model.Sender

val UserBubbleColor = Color(0xFF34333B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatFlowScreen(
    onBackClick: () -> Unit,
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    viewModel: InterviewViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    var showSettings by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.isLoading) {
        val totalItems = state.messages.size + if (state.isLoading) 1 else 0
        if (totalItems > 0) {
            listState.scrollToItem(totalItems - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(onBackClick = onBackClick, onMenuClick = { showSettings = true })
        },
//        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            ChatInputBar(
                text = state.inputText,
                onTextChange = { viewModel.onInputChanged(it) },
                onSend = {
                    if (state.inputText.isNotBlank()) {
                        viewModel.sendMessage()
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.messages, key = { it.id }) { message ->
                    if (message.sender == Sender.USER) {
                        UserMessageRow(message)
                    } else {
                        AssistantMessageRow(message)
                    }
                }

                if (state.isLoading) {
                    item(key = "typing_indicator") { AssistantTypingRow() }
                }
            }
        }

        if (showSettings) {
            SettingsDialog(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
                onClearChat = { viewModel.clearChat() },
                onDismiss = { showSettings = false }
            )
        }
    }
}

@Composable
fun ChatHeader(onBackClick: () -> Unit, onMenuClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        colors =
                            IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // Menu/Settings button
                IconButton(
                    onClick = onMenuClick,
                    colors =
                        IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun UserMessageRow(message: ChatMessage) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .widthIn(max = 280.dp)
        ) { Text(text = message.text ?: "", color =  MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp) }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = message.timestamp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}

@Composable
fun AssistantMessageRow(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {

            Box(
                modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .widthIn(max = 260.dp)
            ) {
                Text(
                    text = message.text ?: "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.timestamp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun AssistantTypingRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            TypingIndicator()
        }
    }
}

@Composable
fun TypingIndicator() {
    Box(
        modifier =
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "typing")
            val dotScale1 by
            infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = 600
                                0.6f at 0
                                1f at 200
                                0.6f at 400
                            },
                        repeatMode = RepeatMode.Reverse
                    ),
                label = "dot1"
            )
            val dotScale2 by
            infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = 600
                                0.6f at 150
                                1f at 350
                                0.6f at 550
                            },
                        repeatMode = RepeatMode.Reverse
                    ),
                label = "dot2"
            )
            val dotScale3 by
            infiniteTransition.animateFloat(
                initialValue = 0.6f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = 600
                                0.6f at 300
                                1f at 500
                                0.6f at 600
                            },
                        repeatMode = RepeatMode.Reverse
                    ),
                label = "dot3"
            )

            val dotColor = UserBubbleColor.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .size(8.dp * dotScale1)
                    .background(dotColor, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(8.dp * dotScale2)
                    .background(dotColor, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(8.dp * dotScale3)
                    .background(dotColor, CircleShape)
            )
        }
    }
}

@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* emoji picker */ }) {
                Icon(
                    imageVector = Icons.Default.PermMedia,
                    contentDescription = "Emoji",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextField(
                value = text,
                onValueChange = { onTextChange(it) },
                placeholder = {
                    Text(
                        "Reply ...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier.weight(1f),
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
            )

            IconButton(
                onClick = onSend,
                colors = IconButtonDefaults.iconButtonColors(containerColor = UserBubbleColor),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsDialog(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onClearChat: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Interview Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Selection Title
                Text(
                    text = "App Theme",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Theme Selection Options
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AppTheme.entries.forEach { theme ->
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onThemeChange(theme) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (currentTheme == theme),
                                onClick = { onThemeChange(theme) },
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = UserBubbleColor
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text =
                                    when (theme) {
                                        AppTheme.LIGHT -> "Light Mode"
                                        AppTheme.DARK -> "Dark Mode"
                                        AppTheme.SYSTEM -> "System Default"
                                    },
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Reset Action Section
                Text(
                    text = "Conversation",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = {
                        onClearChat()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Reset Interview") }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = UserBubbleColor) }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
}
