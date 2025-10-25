package com.balikllama.xpguiderdemo.ui.designsystem

import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

// ============================================
// LIGHT THEME (Aydınlık Mod)
// ============================================
val LightColors = lightColorScheme(
    // Ana renkler (butonlar için)
    primary = Color(0xFF2563EB),          // Mavi - Ana butonlar (Evet/Hayır/Kararsızım)
    onPrimary = Color.White,              // Buton üstündeki yazı

    // Arka plan
    background = Color(0xFFF8FAFC),       // Açık gri - Ana arka plan
    onBackground = Color(0xFF0F172A),     // Arka plan üstündeki yazı

    // Yüzey (kartlar için)
    surface = Color.White,                // Test kartları, chat baloncukları
    onSurface = Color(0xFF0F172A),        // Kart üstündeki yazı
    surfaceVariant = Color(0xFFE2E8F0),   // İkincil yüzey (kullanıcı chat balonu)
    onSurfaceVariant = Color(0xFF475569), // İkincil yüzey yazısı

    // Hatalar (form validasyon için)
    error = Color(0xFFDC2626),
    onError = Color.White,

    // Outline (chat input border vb.)
    outline = Color(0xFFCBD5E1),
)

// ============================================
// DARK THEME (Karanlık Mod)
// ============================================
val DarkColors = darkColorScheme(
    // Ana renkler
    primary = Color(0xFF60A5FA),          // Açık mavi
    onPrimary = Color(0xFF1E3A8A),        // Koyu mavi yazı

    // Arka plan
    background = Color(0xFF0F172A),       // Koyu mavi-siyah
    onBackground = Color(0xFFE2E8F0),     // Açık yazı

    // Yüzey
    surface = Color(0xFF1E293B),          // Koyu gri kartlar
    onSurface = Color(0xFFE5E7EB),        // Açık yazı
    surfaceVariant = Color(0xFF334155),   // Daha açık gri (kullanıcı balonu)
    onSurfaceVariant = Color(0xFF94A3B8), // Gri yazı

    // Hatalar
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),

    // Outline
    outline = Color(0xFF475569),
)
