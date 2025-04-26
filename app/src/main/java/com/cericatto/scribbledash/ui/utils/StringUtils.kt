package com.cericatto.scribbledash.ui.utils

import com.cericatto.scribbledash.R

fun getOopsMehList(): List<Int> {
	return listOf(
		R.string.feedback_oops_1,
		R.string.feedback_oops_2,
		R.string.feedback_oops_3,
		R.string.feedback_oops_4,
		R.string.feedback_oops_5,
		R.string.feedback_oops_6,
		R.string.feedback_oops_7,
		R.string.feedback_oops_8,
		R.string.feedback_oops_9,
		R.string.feedback_oops_10
	)
}

fun getGoodGreatList(): List<Int> {
	return listOf(
		R.string.feedback_good_1,
		R.string.feedback_good_2,
		R.string.feedback_good_3,
		R.string.feedback_good_4,
		R.string.feedback_good_5,
		R.string.feedback_good_6,
		R.string.feedback_good_7,
		R.string.feedback_good_8,
		R.string.feedback_good_9,
		R.string.feedback_good_10
	)
}

fun getWoohooList(): List<Int> {
	return listOf(
		R.string.feedback_woohoo_1,
		R.string.feedback_woohoo_2,
		R.string.feedback_woohoo_3,
		R.string.feedback_woohoo_4,
		R.string.feedback_woohoo_5,
		R.string.feedback_woohoo_6,
		R.string.feedback_woohoo_7,
		R.string.feedback_woohoo_8,
		R.string.feedback_woohoo_9,
		R.string.feedback_woohoo_10
	)
}

fun getDrawingTitleId(score: Int) = when (score) {
	in 0 .. 39 -> R.string.oops
	in 40 .. 69 -> R.string.meh
	in 70 .. 79 -> R.string.good
	in 80 .. 89 -> R.string.great
	else -> R.string.woohoo
}

fun getDrawingMessageId(score: Int) = when (score) {
	in 0 .. 39 -> getOopsMehList().random()
	in 40 .. 69 -> getOopsMehList().random()
	in 70 .. 79 -> getGoodGreatList().random()
	in 80 .. 89 -> getGoodGreatList().random()
	else -> getWoohooList().random()
}