package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.cericatto.scribbledash.model.PathData
import com.cericatto.scribbledash.model.initOffsetList

data class DrawScreenState(
	val selectedColor: Color = Color.Black,
	val currentPath: PathData? = null,
	val paths: List<PathData> = emptyList()
)

fun initPathList(): List<PathData> {
	val move = PathData(
		path = initOffsetList()
	)
	return listOf(move)
}

fun initPathList(
	offsetList: List<Offset>
): List<PathData> {
	val data = PathData(
		path = offsetList
	)
	return listOf(data)
}