package com.example.viewcoach.presentation.QuestionScreen

enum class InterviewTrack(
    val title: String,
    val subtitle: String
) {
    FLUTTER(
        title = "Flutter Developer",
        subtitle = "Dart, Widgets, State Management, Firebase, Architecture"
    ),

    ANDROID(
        title = "Android Native",
        subtitle = "Kotlin, Compose, Coroutines, Hilt, Room, MVVM"
    ),

    SWIFTUI(
        title = "iOS SwiftUI",
        subtitle = "Swift, SwiftUI, Combine, Async/Await, MVVM"
    )
}