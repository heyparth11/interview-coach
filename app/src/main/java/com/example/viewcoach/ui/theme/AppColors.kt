package com.example.viewcoach.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val icons: Color,
    val iconsBg: Color,
    val chatBox: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No AppColors provided")
}