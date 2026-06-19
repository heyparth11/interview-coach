package com.example.viewcoach.presentation.QuestionScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

//@Composable
//fun InterviewScreen(
//    viewModel: InterviewViewModel = hiltViewModel(),
//) {
//
//    val state = viewModel.uiState
//
//    Column() {
//
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            reverseLayout = false
//        ) {
//
//            items(
//                items = state.messages,
//                key = { it.id }
//            ) { message ->
//
//                ChatBubble(message)
//            }
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            OutlinedTextField(
//                value = state.inputText,
//                onValueChange = viewModel::onInputChanged,
//                modifier = Modifier.weight(1f),
//                placeholder = {
//                    Text("Type your answer...")
//                }
//            )
//
//            IconButton(
//                onClick = {viewModel.sendMessage()}
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.Send,
//                    contentDescription = null
//                )
//            }
//        }
//
//    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//
//        Text(
//            text = state.question,
//            style = MaterialTheme.typography.headlineSmall
//        )
//
//        OutlinedTextField(
//            value = state.answer,
//            onValueChange = viewModel::onAnswerChanged,
//            modifier = Modifier.fillMaxWidth(),
//            label = {
//                Text("Your Answer")
//            },
//            minLines = 5
//        )
//
//        Button(
//            onClick = viewModel::submitAnswer,
//            enabled = !state.isLoading,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Submit")
//        }
//
//        if (state.isLoading) {
//            CircularProgressIndicator()
//        }
//
//        if (state.result.isNotBlank()) {
//
//            Card(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//
//                Text(
//                    text = state.result,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
//}