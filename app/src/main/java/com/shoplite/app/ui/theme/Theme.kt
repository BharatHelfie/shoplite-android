package com.shoplite.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShopLiteColors = lightColorScheme(
    primary = Color(0xFF3949AB),
    secondary = Color(0xFF00897B),
)

@Composable
fun ShopLiteTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ShopLiteColors, content = content)
}
