package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SadraColorScheme = darkColorScheme(
  primary = SkyBlue,
  onPrimary = Slate900,
  primaryContainer = RoyalBlue,
  onPrimaryContainer = Color.White,
  secondary = EmeraldGreen,
  onSecondary = Color.Black,
  secondaryContainer = Slate700,
  onSecondaryContainer = Color.White,
  tertiary = AmberAccent,
  onTertiary = Color.Black,
  background = Slate900,
  onBackground = Color.White,
  surface = Slate800,
  onSurface = Color.White,
  surfaceVariant = Slate700,
  onSurfaceVariant = Slate400,
  outline = Slate600,
  error = CrimsonRed,
  onError = Color.White,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SadraColorScheme,
    typography = Typography,
    content = content
  )
}

