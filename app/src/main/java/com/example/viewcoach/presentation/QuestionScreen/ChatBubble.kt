package com.example.viewcoach.presentation.QuestionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viewcoach.domain.model.ChatMessage
import com.example.viewcoach.domain.model.Sender

@Composable
fun ChatBubble(
    message: ChatMessage
) {

    val isUser = message.sender == Sender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (isUser) Arrangement.End
            else Arrangement.Start
    ) {

        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {

            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}