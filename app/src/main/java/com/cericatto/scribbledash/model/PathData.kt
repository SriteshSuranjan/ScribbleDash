package com.cericatto.scribbledash.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class PathData(
//	val id: String = System.currentTimeMillis().toString(),
	val id: Int = 0,
	val color: Color = Color.Black,
	val path: List<Offset> = emptyList<Offset>()
)

fun initOffsetList() = List(50) { Offset(Random.nextFloat() * 400, Random.nextFloat() * 400) }