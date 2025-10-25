package com.balikllama.xpguiderdemo.ui.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


// ============================================
// 1. ÖZEL ANLAMSAL RENKLERİ TANIMLAMA
// ============================================

// Özel renklerimizi tutacak veri sınıfı
data class CustomColors(
    val yesButton: Color,
    val onYesButton: Color,
    val neutralButton: Color,
    val onNeutralButton: Color,
    val noButton: Color,
    val onNoButton: Color
)

// Aydınlık tema için bu renkleri tanımla
private val LightCustomColors = CustomColors(
    yesButton = Color(0xFF4CAF50),       // Yeşilimsi
    onYesButton = Color.White,
    neutralButton = Color(0xFFFFC107),   // Sarımsı
    onNeutralButton = Color.Black,
    noButton = Color(0xFFF44336),        // Kırmızımsı
    onNoButton = Color.White
)

// Karanlık tema için bu renkleri tanımla
private val DarkCustomColors = CustomColors(
    yesButton = Color(0xFF81C784),       // Açık Yeşil
    onYesButton = Color(0xFF1B5E20),
    neutralButton = Color(0xFFFFE082),   // Açık Sarı
    onNeutralButton = Color(0xFF212121),
    noButton = Color(0xFFE57373),        // Açık Kırmızı
    onNoButton = Color(0xFF3E2723)
)

// Bu renkleri CompositionLocal ile tema geneline yay
private val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }

// MaterialTheme objesini genişleterek bu renklere kolay erişim sağla
val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current


// ============================================
// 2. MEVCUT AppTheme FONKSİYONUNU GÜNCELLEME
// ============================================

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Renk seçimi (otomatik dark mode desteği)
    val colorScheme = if (darkTheme) DarkColors else LightColors

    // Özel renklerimizin de aydınlık/karanlık mod değişimini yönet
    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

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

    // Özel renkleri `CompositionLocalProvider` ile tema geneline sağlıyoruz.
    // MaterialTheme bunun içinde kalmalı.
    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = AppTypography, // Typography.kt'den gelen tipografi
            content = content
        )
    }
}

/**
    // Material 3 teması
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        content = content
    )
**/