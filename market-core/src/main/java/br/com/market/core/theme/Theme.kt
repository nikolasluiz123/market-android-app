package br.com.market.core.theme

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

private val darkColorScheme = darkColorScheme(
    primary = GREY_900,
    primaryContainer = GREY_900,
    onPrimary = GREY_50,
    onPrimaryContainer = Color.White,
    secondary = GREY_800,
    secondaryContainer = GREY_800,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    tertiary = GREY_400,
    tertiaryContainer = GREY_400,
    onTertiary = Color.White,
    onTertiaryContainer = Color.White,
    background = GREY_600,
    onBackground = Color.White,
    outline = Color.White,
    outlineVariant = Color.White,
    onSurface = Color.White,
    surface = GREY_600
)

private val lightColorScheme = lightColorScheme(
    primary = BLUE_800,
    primaryContainer = BLUE_800,
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = BLUE_600,
    secondaryContainer = BLUE_600,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    tertiary = BLUE_300,
    tertiaryContainer = BLUE_300,
    onTertiary = Color.White,
    onTertiaryContainer = Color.White,
    background = GREY_100,
    onBackground = GREY_900,
    outline = GREY_600,
    outlineVariant = GREY_600,
    onSurface = GREY_800,
    surface = GREY_100,
)

@Composable
fun MarketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}