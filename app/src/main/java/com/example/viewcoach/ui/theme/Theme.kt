package com.example.viewcoach.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


val LightAppColors = AppColors(
    background = LightBackground,
    surface = LightSurface,
    primary = LightPrimary,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    icons = LightIcons,
    iconsBg = LightIconsBg,
    chatBox = LightChatBox
)

val DarkAppColors = AppColors(
    background = DarkBackground,
    surface = DarkSurface,
    primary = DarkPrimary,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    icons = DarkIcons,
    iconsBg = DarkIconsBg,
    chatBox = DarkChatBox
)

@Composable
fun ViewCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set dynamic color default to false to respect user colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = DarkPrimary,
            background = DarkBackground,
            surface = DarkSurface,
            onBackground = DarkTextPrimary,
            onSurface = DarkTextPrimary,
            surfaceVariant = DarkChatBox,
            onSurfaceVariant = DarkTextPrimary,
            secondaryContainer = DarkIconsBg,
            onSecondaryContainer = DarkBackground
        )
        else -> lightColorScheme(
            primary = LightPrimary,
            background = LightBackground,
            surface = LightSurface,
            onBackground = LightTextPrimary,
            onSurface = LightTextPrimary,
            surfaceVariant = LightChatBox,
            onSurfaceVariant = LightTextPrimary,
            secondaryContainer = LightIconsBg,
            onSecondaryContainer = LightBackground
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}