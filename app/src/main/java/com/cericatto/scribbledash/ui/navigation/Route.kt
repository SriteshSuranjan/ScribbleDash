package com.cericatto.scribbledash.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
	@Serializable
	data object HomeScreen: Route

	@Serializable
	data object DifficultyScreen: Route

	@Serializable
	data object DrawScreen: Route

	@Serializable
	data class ResultScreen(
		val paths: String? = null
	): Route

	@Serializable
	data object StatisticsScreen: Route
}