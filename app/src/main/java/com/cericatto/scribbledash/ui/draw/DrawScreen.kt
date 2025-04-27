package com.cericatto.scribbledash.ui.draw

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.common.ClearCanvasButton
import com.cericatto.scribbledash.ui.common.CloseScreenIcon
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ScribbleSubtitleText
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.common.UndoRedoButton
import com.cericatto.scribbledash.ui.common.UndoRedoType
import com.cericatto.scribbledash.ui.common.canvasModifier
import com.cericatto.scribbledash.ui.common.drawScribblePath
import com.cericatto.scribbledash.ui.common.getCanvasSize
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.bagelFatOneRegularFont
import com.cericatto.scribbledash.ui.theme.drawBackground
import com.cericatto.scribbledash.ui.theme.scribbleSecondsLeftTextColor

@Composable
fun DrawScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: DrawScreenViewModel = hiltViewModel()
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
	DrawScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@Composable
private fun DrawScreen(
	modifier: Modifier = Modifier,
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState
) {
	DrawScreenContent(
		modifier = modifier,
		onAction = onAction,
		state = state
	)
}

@Composable
private fun DrawScreenContent(
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState,
	modifier: Modifier = Modifier
) {
	Column(
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.background(color = drawBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		CloseScreenIcon(
			onClose = { onAction(DrawScreenAction.NavigateUp) },
			backgroundColor = drawBackground
		)
		val title = if (state.drawMode) {
			stringResource(R.string.time_to_draw)
		} else {
			stringResource(R.string.ready_set)
		}
		ScribbleTitleText(
			text = title,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 100.dp)
		)
		DrawCanvas(
			onAction = onAction,
			state = state
		)
		Box(
			contentAlignment = Alignment.BottomStart,
			modifier = Modifier.fillMaxSize()
		) {
			if (state.drawMode) {
				BottomDrawMenu(
					onAction = onAction,
					state = state
				)
			} else {
				TimerMenu(
					state = state
				)
			}
		}
	}
}

@Composable
private fun TimerMenu(
	state: DrawScreenState
) {
	Column(
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.fillMaxSize()
	) {
		ScribbleSubtitleText(
			text = if (state.drawMode) {
				stringResource(R.string.your_drawing)
			} else {
				stringResource(R.string.example)
			},
			fontFamily = null,
			modifier = Modifier.padding(top = 10.dp)
		)
		ScribbleSubtitleText(
			text = stringResource(R.string.seconds_left, state.timer),
			textColor = scribbleSecondsLeftTextColor,
			fontSize = 30.sp,
			fontFamily = bagelFatOneRegularFont,
			fontWeight = FontWeight.Medium,
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 20.dp)
		)
	}
}

@Composable
private fun BottomDrawMenu(
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState,
	modifier: Modifier = Modifier
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.padding(vertical = 20.dp, horizontal = 5.dp)
			.fillMaxWidth()
	) {
		UndoRedoButton(
			modifier = Modifier.weight(1f),
			type = UndoRedoType.UNDO,
			state = state,
			onClick = { onAction(DrawScreenAction.OnUndoButtonClicked) }
		)
		UndoRedoButton(
			modifier = Modifier.weight(1f),
			type = UndoRedoType.REDO,
			state = state,
			onClick = { onAction(DrawScreenAction.OnRedoButtonClicked) }
		)
		ClearCanvasButton(
			modifier = Modifier.weight(4f),
			state = state,
			onClick = {
				val isEnabled = state.paths.isNotEmpty()
				if (isEnabled) {
//					onAction(DrawScreenAction.OnClearCanvasClick)
					onAction(DrawScreenAction.OnNavigateToResult)
				}
			}
		)
	}
}

@Composable
private fun DrawCanvas(
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState
) {
	val canvasSize = getCanvasSize()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 30.dp)
			.padding(horizontal = 10.dp)
			.shadow(
				elevation = 5.dp,
				shape = RoundedCornerShape(30.dp),
			)
//			.clip(RoundedCornerShape(30.dp))
	) {
		GridCanvas(
			canvasSize = canvasSize,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun GridCanvas(
	modifier: Modifier = Modifier,
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState,
	canvasSize: Dp = 300.dp,
	gridSize: Int = 3,
	lineColor: Color = Color.LightGray.copy(alpha = 0.4f),
	lineThickness: Float = 3f
) {
	val context = LocalContext.current
	var cellSize by remember { mutableFloatStateOf(0f) }
	Canvas(
		modifier = modifier
			.width(canvasSize)
			.aspectRatio(1f)
			.then(
				if (state.drawMode) {
					Modifier.pointerInput(true) {
						detectDragGestures(
							onDragStart = {
								onAction(DrawScreenAction.OnNewPathStart)
							},
							onDragEnd = {
								onAction(DrawScreenAction.OnPathEnd)
							},
							onDrag = { change, _ ->
								onAction(DrawScreenAction.OnDraw(change.position))
							},
							onDragCancel = {
								onAction(DrawScreenAction.OnPathEnd)
							}
						)
					}
				} else {
					Modifier
				}
			)
			.canvasModifier()
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

		// Draw paths.
		if (state.drawMode) {
			state.paths.fastForEach { pathData ->
				drawScribblePath(
					path = pathData.path,
					color = pathData.color,
				)
			}
			state.currentPath?.let {
				drawScribblePath(
					path = it.path,
					color = it.color
				)
			}
		} else {
			val drawable: Drawable? = ContextCompat.getDrawable(
				context, state.drawableId
			)
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

fun DrawScope.customLines(
	gridSize: Int,
	cellSize: Float,
	lineColor: Color,
	lineThickness: Float,
	canvasWidth: Float,
	canvasHeight: Float
) {
	// Draw vertical lines.
	for (i in 1..gridSize - 1) {
		val xPosition = i * cellSize
		drawLine(
			color = lineColor,
			start = Offset(xPosition, 0f),
			end = Offset(xPosition, canvasHeight),
			strokeWidth = lineThickness
		)
	}

	// Draw horizontal lines.
	for (i in 1..gridSize - 1) {
		val yPosition = i * cellSize
		drawLine(
			color = lineColor,
			start = Offset(0f, yPosition),
			end = Offset(canvasWidth, yPosition),
			strokeWidth = lineThickness
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun DrawScreenContentPreview() {
	DrawScreenContent(
		modifier = Modifier,
		onAction = {},
		state = DrawScreenState().copy(drawMode = true)
	)
}