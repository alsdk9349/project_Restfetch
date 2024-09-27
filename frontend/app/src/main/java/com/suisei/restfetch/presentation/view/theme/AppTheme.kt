package com.suisei.restfetch.presentation.view.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun backgroundColor(): Color {
    return Color(0XFFFFFDF8)
}

@Composable
fun buttonTransparentTheme(): ButtonColors {
    return ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
}