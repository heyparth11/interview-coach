package com.example.viewcoach.presentation.QuestionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.example.viewcoach.R

@Composable
fun TrackSelectionScreen(
    onTrackSelected: (InterviewTrack) -> Unit
) {

    val tracks = InterviewTrack.entries

    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "AI Interview Coach",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose a learning track and start answering questions. The AI will adapt from beginner to advanced concepts.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            items(tracks) { track ->

                TrackCard(
                    track = track,
                    onClick = {
                        onTrackSelected(track)
                    }
                )

//                ElevatedCard(
//                    onClick = {
//                        onTrackSelected(track)
//                    }
//                ) {
//
//                    Text(
//                        text = track.title,
//                        modifier = Modifier.padding(20.dp),
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun TrackCard(
    track: InterviewTrack,
    onClick: () -> Unit
) {

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(
                    when (track) {
                        InterviewTrack.FLUTTER -> R.drawable.flutter_logo
                        InterviewTrack.ANDROID -> R.drawable.android_logo
                        InterviewTrack.SWIFTUI -> R.drawable.apple_logo_black
                    }

                ),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

//            Text(
//                text = when (track) {
//                    InterviewTrack.FLUTTER -> "🦋"
//                    InterviewTrack.ANDROID -> "🤖"
//                    InterviewTrack.SWIFTUI -> "\uf8ff"
//                },
//                style = MaterialTheme.typography.headlineLarge
//            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = track.subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}
