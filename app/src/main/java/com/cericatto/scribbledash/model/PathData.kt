package com.cericatto.scribbledash.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathData(
//	val id: String = System.currentTimeMillis().toString(),
	val id: Int = 0,
	val color: Color = Color.Black,
	val path: List<Offset> = emptyList<Offset>()
)

fun initOffsetList(): List<Offset> {
	return listOf(
		Offset(0f, 0f),
		Offset(1f, 1f)
	)
}