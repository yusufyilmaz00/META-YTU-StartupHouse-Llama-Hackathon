package com.balikllama.b1demo.ui.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Renk seçimi (otomatik dark mode desteği)
    val colorScheme = if (darkTheme) DarkColors else LightColors

    // Şekiller (tüm köşeler için)
    val shapes = Shapes(
        small = RoundedCornerShape(Radius.S),      // 8dp
        medium = RoundedCornerShape(Radius.M),     // 12dp - EN ÇOK KULLANILACAK
        large = RoundedCornerShape(Radius.L),      // 16dp
        extraLarge = RoundedCornerShape(Radius.XL) // 24dp - Chat baloncukları
    )

    // Sistem çubuklarını (status bar + navigation bar) arka plan rengine ayarla
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            // Açık tema ise status bar ikonlarını koyu yap
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    // Material 3 teması
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        content = content
    )
}