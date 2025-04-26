package com.cericatto.scribbledash.ui.result

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.model.initOffsetList
import com.cericatto.scribbledash.ui.common.CloseScreenIcon
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ResultCanvasType
import com.cericatto.scribbledash.ui.common.ScribbleSubtitleText
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.common.canvasModifier
import com.cericatto.scribbledash.ui.common.drawScribblePath
import com.cericatto.scribbledash.ui.common.getCanvasSize
import com.cericatto.scribbledash.ui.draw.customLines
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.bagelFatOneRegularFont
import com.cericatto.scribbledash.ui.theme.drawBackground
import com.cericatto.scribbledash.ui.theme.tryAgainButtonBackground
import com.cericatto.scribbledash.ui.utils.contentColor
import com.cericatto.scribbledash.ui.utils.getDrawingMessageId
import com.cericatto.scribbledash.ui.utils.getDrawingTitleId

@Composable
fun ResultScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: ResultScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	ObserveAsEvents(viewModel.events) { event ->
		when (event) {
			is UiEvent.NavigateUp -> onNavigateUp()
			is UiEvent.Navigate -> onNavigate(event.route)
			else -> Unit
		}
	}

//	DynamicStatusBarColor()
	ResultScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ResultScreen(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState
) {
	if (state.loading) {
		Box(
			modifier = Modifier
				.padding(vertical = 20.dp)
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(
				color = contentColor(),
				strokeWidth = 4.dp,
				modifier = Modifier.size(64.dp)
			)
		}
	} else {
		ResultScreenContent(
			modifier = modifier,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun ResultScreenContent(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState
) {
	val title = stringResource(getDrawingTitleId(state.score))
	val message = stringResource(getDrawingMessageId(state.score))
	println("Message: $message")
	Column(
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.background(color = drawBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		CloseScreenIcon(
			onClose = { onAction(ResultScreenAction.NavigateUp) },
			backgroundColor = drawBackground
		)
		ScribbleTitleText(
			text = "${state.score}%",
			fontSize = 66.sp,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 60.dp)
		)
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
//				.background(Color.Cyan)
				.fillMaxWidth()
		) {
			DrawCanvas(
				modifier = Modifier
					.rotate(-10f)
					.padding(bottom = 60.dp)
					.align(Alignment.CenterStart),
				onAction = onAction,
				state = state,
				type = ResultCanvasType.EXAMPLE
			)
			DrawCanvas(
				modifier = Modifier
					.rotate(10f)
					.align(Alignment.CenterEnd),
				onAction = onAction,
				state = state,
				type = ResultCanvasType.DRAWING
			)
		}
		ScribbleTitleText(
			text = title,
			modifier = Modifier
				.fillMaxWidth()
		)
		ScribbleSubtitleText(
			text = message,
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
		)
		TryAgainButton(
			modifier = Modifier.fillMaxWidth()
				.padding(horizontal = 20.dp)
				.padding(top = 40.dp, bottom = 10.dp)
		)
	}
}

@Composable
private fun DrawCanvas(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState,
	type: ResultCanvasType
) {
	val canvasSize = getCanvasSize() / 2
	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
			.padding(top = 30.dp)
			.padding(horizontal = 10.dp)
//			.shadow(
//				elevation = 1.dp,
//				shape = RoundedCornerShape(30.dp),
//			)
	) {
		GridCanvas(
			canvasSize = canvasSize,
			onAction = onAction,
			state = state,
			type = type
		)
	}
}

@Composable
private fun GridCanvas(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState,
	canvasSize: Dp = 300.dp,
	gridSize: Int = 3,
	lineColor: Color = Color.LightGray.copy(alpha = 0.4f),
	lineThickness: Float = 1f,
	type: ResultCanvasType
) {
	val titleText = if (type == ResultCanvasType.EXAMPLE) {
		stringResource(R.string.example)
	} else {
		stringResource(R.string.drawing)
	}
	val context = LocalContext.current
	val drawable: Drawable? = ContextCompat.getDrawable(
		context, R.drawable.whale
	)
	var cellSize by remember { mutableFloatStateOf(0f) }
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		ScribbleSubtitleText(
			text = titleText,
			modifier = Modifier.padding(bottom = 5.dp)
		)
		Canvas(
			modifier = modifier
				.width(canvasSize)
				.aspectRatio(1f)
				.canvasModifier(borderPadding = 5.dp)
		) {
			// Calculate the cell size based on the canvas dimensions.
			val canvasWidth = size.width
			val canvasHeight = size.height
			cellSize = canvasWidth / gridSize
			customLines(
				gridSize = gridSize,
				cellSize = cellSize,
				lineColor = lineColor,
				lineThickness = lineThickness,
				canvasWidth = canvasWidth,
				canvasHeight = canvasHeight
			)
			if (type == ResultCanvasType.DRAWING) {
				drawScribblePath(
					path = initOffsetList(),
					color = Color.Black
				)
			} else {
				drawable?.let {
					// Convert Drawable to Bitmap
					val bitmap = createBitmap(size.width.toInt(), size.height.toInt())
					val canvas = android.graphics.Canvas(bitmap)
					it.setBounds(0, 0, size.width.toInt(), size.height.toInt())
					it.draw(canvas)
					drawImage(
						image = bitmap.asImageBitmap()
					)
				}
			}
		}
	}
}

@Composable
private fun TryAgainButton(
	modifier: Modifier = Modifier,
	text: String = "Try Again",
	onClick: () -> Unit = {}
) {
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
				color = tryAgainButtonBackground,
				shape = RoundedCornerShape(15.dp)
			)
			.padding(horizontal = 20.dp, vertical = 15.dp)
	)
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenContentPreview() {
	ResultScreenContent(
		modifier = Modifier,
		onAction = {},
		state = ResultScreenState()
	)
}