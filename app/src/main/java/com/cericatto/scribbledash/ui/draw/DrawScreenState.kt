package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import com.cericatto.scribbledash.model.Move
import com.cericatto.scribbledash.model.initOffsetList

data class DrawScreenState(
	val loading : Boolean = true,
	val moves: List<Move> = emptyList<Move>()
)

fun initMoveList(): List<Move> {
	val move = Move(
		points = initOffsetList()
	)
	return listOf(move)
}

fun initMoveList(
	offsetList: List<Offset>
): List<Move> {
	val move = Move(
		points = offsetList
	)
	return listOf(move)
}