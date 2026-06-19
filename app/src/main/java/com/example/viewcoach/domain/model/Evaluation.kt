package com.example.viewcoach.domain.model

data class Evaluation(
    val technicalAccuracy: Int,
    val communication: Int,
    val completeness: Int,
    val suggestions: String
)