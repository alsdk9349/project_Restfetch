package com.suisei.restfetch.presentation.view.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun backgroundColor(): Color {
    return Color(0XFFFFFDF8)
}

@Composable
fun buttonTransparentTheme(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.Black
    )
}

@Composable
fun BasicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = buttonTransparentTheme(),
    contentPadding: PaddingValues = PaddingValues(8.dp),
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp)),
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        colors = colors,
        contentPadding = contentPadding
    ) {
        content()
    }
}