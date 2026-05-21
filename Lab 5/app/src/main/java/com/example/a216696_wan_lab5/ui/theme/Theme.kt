package com.example.a216696_wan_lab5.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── LinguaQuest Dark Colour Scheme
private val LinguaQuestDarkColorScheme = darkColorScheme(
    primary          = Green80,
    onPrimary        = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,

    secondary        = Color(0xFF6BFFB8),
    onSecondary      = Blue20,
    secondaryContainer = Color(0xFF0D7A48),
    onSecondaryContainer = Blue90,

    tertiary         = DarkGreen80,
    onTertiary       = DarkGreen20,
    tertiaryContainer = DarkGreen30,
    onTertiaryContainer = DarkGreen90,

    error            = Red80,
    onError          = Red20,
    errorContainer   = Red30,
    onErrorContainer = Red90,

    background       = Color(0xFF0F1923),
    onBackground     = Grey90,

    surface          = Color(0xFF1A2535),
    onSurface        = Grey80,

    surfaceVariant   = GreenGrey30,
    onSurfaceVariant = GreenGrey80,

    outline          = GreenGrey50
)

// ── LinguaQuest Light Colour Scheme ──
private val LinguaQuestLightColorScheme = lightColorScheme(
    primary          = Green40,
    onPrimary        = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,

    secondary        = Blue40,
    onSecondary      = Color.White,
    secondaryContainer = Blue90,
    onSecondaryContainer = Blue10,

    tertiary         = DarkGreen40,
    onTertiary       = Color.White,
    tertiaryContainer = DarkGreen90,
    onTertiaryContainer = DarkGreen10,

    error            = Red40,
    onError          = Color.White,
    errorContainer   = Red90,
    onErrorContainer = Red10,

    background       = Grey99,
    onBackground     = Grey10,

    surface          = Grey99,
    onSurface        = Grey10,

    surfaceVariant   = GreenGrey90,
    onSurfaceVariant = GreenGrey30,

    outline          = GreenGrey50
)

@Composable
fun A216696_Wan_Lab5Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> LinguaQuestDarkColorScheme
        else      -> LinguaQuestLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}