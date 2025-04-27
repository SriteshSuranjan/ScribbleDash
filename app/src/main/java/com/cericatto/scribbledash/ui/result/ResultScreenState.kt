package com.cericatto.scribbledash.ui.result

import com.cericatto.scribbledash.model.PathData

data class ResultScreenState(
	val loading : Boolean = true,
	val paths: List<PathData> = emptyList(),
	val score: Int = randomScore()
)

fun randomScore(): Int {
	val range = 0..100
	return range.random()
}