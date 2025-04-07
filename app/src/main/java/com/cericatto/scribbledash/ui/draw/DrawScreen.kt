package com.cericatto.scribbledash.ui.draw

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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.model.initOffsetList
import com.cericatto.scribbledash.ui.common.ClearCanvasButton
import com.cericatto.scribbledash.ui.common.CloseScreenIcon
import com.cericatto.scribbledash.ui.common.DynamicStatusBarColor
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.common.UndoRedoButton
import com.cericatto.scribbledash.ui.common.UndoRedoType
import com.cericatto.scribbledash.ui.common.getCanvasSize
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.drawBackground
import kotlin.math.abs

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

	DynamicStatusBarColor()
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
	modifier: Modifier = Modifier,
	onAction: (DrawScreenAction) -> Unit,
	state: DrawScreenState
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
			onClose = { onAction(DrawScreenAction.NavigateUp) }
		)
		ScribbleTitleText(
			text = "Time to draw!",
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
					onClick = { onAction(DrawScreenAction.OnClearCanvasClick) }
				)
			}
		}
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
	lineColor: Color = Color.LightGray,
	lineThickness: Float = 3f
) {
	var cellSize by remember { mutableFloatStateOf(0f) }
	Canvas(
		modifier = modifier
			.width(canvasSize)
			.aspectRatio(1f)
			.pointerInput(true) {
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
		state.history.fastForEach { pathData ->
			drawPath(
				path = pathData.path,
				color = pathData.color,
			)
		}
		state.currentPath?.let {
			drawPath(
				path = it.path,
				color = it.color
			)
		}
	}
}

private fun DrawScope.drawPath(
	path: List<Offset>,
	color: Color,
	thickness: Float = 10f
) {
	val smoothedPath = Path().apply {
		if (path.isNotEmpty()) {
			moveTo(path.first().x, path.first().y)

			val smoothness = 5
			for(i in 1..path.lastIndex) {
				val from = path[i - 1]
				val to = path[i]
				val dx = abs(from.x - to.x)
				val dy = abs(from.y - to.y)
				if(dx >= smoothness || dy >= smoothness) {
					quadraticTo(
						x1 = (from.x + to.x) / 2f,
						y1 = (from.y + to.y) / 2f,
						x2 = to.x,
						y2 = to.y
					)
				}
			}
		}
	}
	drawPath(
		path = smoothedPath,
		color = color,
		style = Stroke(
			width = thickness,
			cap = StrokeCap.Round,
			join = StrokeJoin.Round
		)
	)
}

private fun Modifier.canvasModifier(
	lineColor: Color = Color.LightGray,
	radius: Dp = 30.dp
) = this
	.background(
		color = lineColor,
		shape = RoundedCornerShape(radius)
	)
	.padding(1.dp)
	.background(
		color = Color.White,
		shape = RoundedCornerShape(radius)
	)
	.padding(10.dp)
	.background(
		color = lineColor,
		shape = RoundedCornerShape(radius)
	)
	.padding(1.dp)
	.background(
		color = Color.White,
		shape = RoundedCornerShape(radius)
	)
	.clipToBounds()

// Extension function on DrawScope to draw the grid lines
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
		state = DrawScreenState()
	)
}