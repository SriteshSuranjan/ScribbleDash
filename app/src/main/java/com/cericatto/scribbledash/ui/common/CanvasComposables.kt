package com.cericatto.scribbledash.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

enum class ResultCanvasType {
	EXAMPLE, DRAWING
}

@Composable
fun getCanvasSize(): Dp {
	val configuration = LocalConfiguration.current
	return if (isLandscapeOrientation()) {
		configuration.screenHeightDp.dp
	} else {
		configuration.screenWidthDp.dp - 50.dp
	}
}

fun Modifier.canvasModifier(
	lineColor: Color = Color.LightGray,
	radius: Dp = 30.dp,
	borderPadding: Dp = 10.dp
) = this
	.background(
		color = lineColor,
		shape = RoundedCornerShape(radius)
	)
	.padding(0.5.dp)
	.background(
		color = Color.White,
		shape = RoundedCornerShape(radius)
	)
	.padding(borderPadding)
	.background(
		color = lineColor,
		shape = RoundedCornerShape(radius)
	)
	.padding(0.5.dp)
	.background(
		color = Color.White,
		shape = RoundedCornerShape(radius)
	)
	.clipToBounds()

fun scalePath(offset: Offset, scaleFactor: Float): Offset {
	return Offset(offset.x * scaleFactor, offset.y * scaleFactor)
}

fun DrawScope.drawScribblePath(
	path: List<Offset>,
	color: Color = Color.LightGray.copy(alpha = 0.4f),
	thickness: Float = 10f,
	factor: Float = 1f,
	errorMargin: Float = 0.02f
) {
	val scaledPaths = path.map { scalePath(it, 1 / factor - errorMargin) }
	val smoothedPath = Path().apply {
		if (scaledPaths.isNotEmpty()) {
			moveTo(scaledPaths.first().x, scaledPaths.first().y)

			val smoothness = 5
			for (i in 1..scaledPaths.lastIndex) {
				val from = scaledPaths[i - 1]
				val to = scaledPaths[i]
				val dx = abs(from.x - to.x)
				val dy = abs(from.y - to.y)
				if (dx >= smoothness || dy >= smoothness) {
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
		),
	)
}