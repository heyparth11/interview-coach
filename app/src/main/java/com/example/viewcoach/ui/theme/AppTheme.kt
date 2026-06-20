package com.example.viewcoach.ui.theme

import androidx.compose.runtime.Composable

object AppTheme {

    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}