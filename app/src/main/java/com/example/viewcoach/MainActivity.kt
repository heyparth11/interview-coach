package com.example.viewcoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viewcoach.presentation.QuestionScreen.ChatFlowScreen
import com.example.viewcoach.presentation.QuestionScreen.TrackSelectionScreen
import com.example.viewcoach.ui.theme.ViewCoachTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val savedThemeName =
                prefs.getString("app_theme", AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name
        val initialTheme =
                try {
                    AppTheme.valueOf(savedThemeName)
                } catch (e: Exception) {
                    AppTheme.SYSTEM
                }

        setContent {
            var appTheme by remember { mutableStateOf(initialTheme) }

            val darkTheme =
                    when (appTheme) {
                        AppTheme.LIGHT -> false
                        AppTheme.DARK -> true
                        AppTheme.SYSTEM -> isSystemInDarkTheme()
                    }

            ViewCoachTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "tracks") {
                    composable("tracks") {
                        TrackSelectionScreen(
                                onTrackSelected = { track ->
                                    navController.navigate("interview/${track.name}")
                                }
                        )
                    }

                    composable(route = "interview/{track}") { backStackEntry ->
                        ChatFlowScreen(
                                onBackClick = { navController.popBackStack() },
                                currentTheme = appTheme,
                                onThemeChange = { newTheme ->
                                    appTheme = newTheme
                                    prefs.edit { putString("app_theme", newTheme.name) }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ViewCoachTheme { Greeting("Android") }
}
