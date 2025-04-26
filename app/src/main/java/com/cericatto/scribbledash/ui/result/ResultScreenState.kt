package com.cericatto.scribbledash.ui.result

data class ResultScreenState(
	val loading : Boolean = true,
	val score: Int = randomScore()
)

fun randomScore(): Int {
	val range = 0..100
	return range.random()
}