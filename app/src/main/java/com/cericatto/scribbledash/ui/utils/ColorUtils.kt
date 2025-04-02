package com.cericatto.scribbledash.ui.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun backgroundColor(): Color {
	return if (isSystemInDarkTheme()) Color.Black.copy(alpha = 0f)
		else Color.White.copy(alpha = 0.4f)
}

@Composable
fun contentColor(): Color {
	return if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.4f)
		else Color.Black.copy(alpha = 0.4f)
}