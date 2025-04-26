package com.cericatto.scribbledash.ui.common

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.model.initOffsetList
import com.cericatto.scribbledash.ui.draw.DrawScreenState
import com.cericatto.scribbledash.ui.draw.initPathList
import com.cericatto.scribbledash.ui.theme.bagelFatOneRegularFont
import com.cericatto.scribbledash.ui.theme.clearCanvasButtonDisabled
import com.cericatto.scribbledash.ui.theme.clearCanvasButtonEnabled
import com.cericatto.scribbledash.ui.theme.closeScreenIconColor
import com.cericatto.scribbledash.ui.theme.drawBackground
import com.cericatto.scribbledash.ui.theme.historyButtonBackgroundDisabled
import com.cericatto.scribbledash.ui.theme.historyButtonBackgroundEnabled
import com.cericatto.scribbledash.ui.theme.historyButtonTintDisabled
import com.cericatto.scribbledash.ui.theme.historyButtonTintEnabled
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.theme.homeBackgroundTitleColor
import com.cericatto.scribbledash.ui.theme.scribbleSubtitleTextColor

enum class UndoRedoType {
	UNDO, REDO
}

@Composable
fun isLandscapeOrientation(): Boolean {
	val context = LocalContext.current
	val orientation = context.resources.configuration.orientation
	return when (orientation) {
		Configuration.ORIENTATION_LANDSCAPE -> true
		else -> false
	}
}

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
	textColor: Color = scribbleSubtitleTextColor,
	fontSize: TextUnit = 16.sp,
	fontWeight: FontWeight = FontWeight.Normal,
	fontFamily: androidx.compose.ui.text.font.FontFamily? = null
) {
	Text(
		text = text,
		style = TextStyle(
			color = textColor,
			fontSize = fontSize,
			textAlign = TextAlign.Center,
			fontWeight = fontWeight,
			fontFamily = fontFamily
		),
		modifier = modifier
	)
}

@Composable
fun CloseScreenIcon(
	onClose: () -> Unit,
	backgroundColor: Color = homeBackground
) {
	Icon(
		imageVector = Icons.Filled.Close,
		contentDescription = "Close screen icon",
		tint = closeScreenIconColor,
		modifier = Modifier
			.clickable { onClose() }
			.padding(top = 5.dp, end = 5.dp)
			.background(
				color = closeScreenIconColor,
				shape = RoundedCornerShape(40.dp)
			)
			.padding(2.dp)
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(40.dp)
			)
			.size(32.dp)
			.padding(4.dp)
	)
}

@Composable
fun UndoRedoButton(
	modifier: Modifier = Modifier,
	type: UndoRedoType,
	state: DrawScreenState,
	onClick: () -> Unit = {}
) {
	val drawableId = when (type) {
		UndoRedoType.UNDO -> R.drawable.reply
		UndoRedoType.REDO -> R.drawable.forward
	}
	val isEnabled = when (type) {
		UndoRedoType.UNDO -> {
			state.paths.isNotEmpty()
		}
		UndoRedoType.REDO -> {
			state.actionStack.isNotEmpty()
		}
	}
	val backgroundColor = if (isEnabled) historyButtonBackgroundEnabled
		else historyButtonBackgroundDisabled
	val tintColor = if (isEnabled) historyButtonTintEnabled
		else historyButtonTintDisabled
	Icon(
		painter = painterResource(drawableId),
		contentDescription = "La la la la la",
		tint = tintColor,
		modifier = modifier
//			.shadow(
//				elevation = 5.dp,
//				shape = RoundedCornerShape(20.dp),
//			)
			.clickable {
				if (isEnabled) {
					onClick()
				}
			}
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(20.dp)
			)
			.padding(15.dp)
			.aspectRatio(1f)
	)
}

@Composable
fun ClearCanvasButton(
	modifier: Modifier = Modifier,
	text: String = "Clear Canvas",
	state: DrawScreenState,
	onClick: () -> Unit = {}
) {
	val isEnabled = state.paths.isNotEmpty()
	val backgroundColor = if (isEnabled) clearCanvasButtonEnabled else clearCanvasButtonDisabled
	Text(
		text = text.uppercase(),
		style = TextStyle(
			color = Color.White,
			fontFamily = bagelFatOneRegularFont,
			fontSize = 20.sp,
			textAlign = TextAlign.Center
		),
		modifier = modifier
			.clickable {
				onClick()
			}
			.shadow(
				elevation = 5.dp,
				shape = RoundedCornerShape(15.dp),
			)
			.background(
				color = Color.White,
				shape = RoundedCornerShape(15.dp)
			)
			.padding(7.dp)
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(15.dp)
			)
			.padding(horizontal = 20.dp, vertical = 15.dp)
	)
}

@Preview(
	name = "Disabled Preview",
	showBackground = true
)
@Composable
fun UndoRedoButtonDisabledPreview() {
	UndoRedoButton(
		type = UndoRedoType.REDO,
		state = DrawScreenState()
	)
}

@Preview(
	name = "Enabled Preview",
	showBackground = true
)
@Composable
fun UndoRedoButtonEnabledPreview() {
	UndoRedoButton(
		type = UndoRedoType.UNDO,
		state = DrawScreenState().copy(
			paths = initPathList(initOffsetList())
		)
	)
}

@Preview(
	name = "Disabled Preview",
	showBackground = true
)
@Composable
fun ClearCanvasButtonDisabledPreview() {
	ClearCanvasButton(
		state = DrawScreenState()
	)
}

@Preview(
	name = "Enabled Preview",
	showBackground = true
)
@Composable
fun ClearCanvasButtonEnabledPreview() {
	ClearCanvasButton(
		state = DrawScreenState().copy(
			paths = initPathList(initOffsetList())
		)
	)
}

@Preview(
	name = "Enabled Preview",
	showBackground = true
)
@Composable
fun CloseScreenIconPreview() {
	CloseScreenIcon(
		onClose = {},
		backgroundColor = drawBackground
	)
}