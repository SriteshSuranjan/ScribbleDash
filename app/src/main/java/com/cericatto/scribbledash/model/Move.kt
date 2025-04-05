package com.cericatto.scribbledash.model

import androidx.compose.ui.geometry.Offset

data class Move(
	val points: List<Offset> = emptyList<Offset>()
)

fun initOffsetList(): List<Offset> {
	return listOf(
		Offset(0f, 0f),
		Offset(1f, 1f)
	)
}