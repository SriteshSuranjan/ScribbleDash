package com.cericatto.scribbledash.ui.common

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.cericatto.scribbledash.ui.difficulty.DifficultyScreenAction
import com.cericatto.scribbledash.ui.theme.bagelFatOneRegularFont
import com.cericatto.scribbledash.ui.theme.closeScreenIconColor
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.theme.homeBackgroundTitleColor
import com.cericatto.scribbledash.ui.theme.scribbleSubtitleTextColor

@Suppress("DEPRECATION")
@Composable
fun DynamicStatusBarColor() {
	val isDarkTheme = isSystemInDarkTheme()
	val window = (LocalView.current.context as Activity).window
	val statusBarColor = if (isDarkTheme) Color.DarkGray else Color.LightGray.copy(alpha = 0.5f)

	LaunchedEffect(isDarkTheme) {
		WindowCompat.setDecorFitsSystemWindows(window, false)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
			// Android 15+: Draw background behind status bar using WindowInsets.
			window.decorView.setBackgroundColor(statusBarColor.toArgb())
			WindowCompat.getInsetsController(window, window.decorView).apply {
				// Control icon appearance.
				isAppearanceLightStatusBars = !isDarkTheme
			}
		} else {
			// Android 14 and below: Use the legacy approach.
			window.statusBarColor = statusBarColor.toArgb()
			WindowCompat.getInsetsController(window, window.decorView).apply {
				isAppearanceLightStatusBars = !isDarkTheme
			}
		}
	}
}

@Composable
fun ScribbleTitleText(
	modifier: Modifier = Modifier,
	text: String,
	fontSize: TextUnit = 44.sp,
	textAlign: TextAlign = TextAlign.Center
) {
	Text(
		text = text,
		style = TextStyle(
			color = homeBackgroundTitleColor,
			fontFamily = bagelFatOneRegularFont,
			fontSize = fontSize,
			textAlign = textAlign
		),
		modifier = modifier
	)
}

@Composable
fun ScribbleSubtitleText(
	modifier: Modifier = Modifier.fillMaxWidth()
		.padding(top = 5.dp),
	text: String,
	fontWeight: FontWeight = FontWeight.Normal
) {
	Text(
		text = text,
		style = TextStyle(
			color = scribbleSubtitleTextColor,
			fontSize = 16.sp,
			textAlign = TextAlign.Center,
			fontWeight = fontWeight
		),
		modifier = modifier
	)
}

@Composable
fun CloseScreenIcon(
	onAction: (DifficultyScreenAction) -> Unit
) {
	Icon(
		imageVector = Icons.Filled.Close,
		contentDescription = "Close screen icon",
		tint = closeScreenIconColor,
		modifier = Modifier
			.clickable {
				onAction(DifficultyScreenAction.NavigateUp)
			}
			.padding(top = 5.dp, end = 5.dp)
			.background(
				color = closeScreenIconColor,
				shape = RoundedCornerShape(40.dp)
			)
			.padding(2.dp)
			.background(
				color = homeBackground,
				shape = RoundedCornerShape(40.dp)
			)
			.size(32.dp)
			.padding(4.dp)
	)
}