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
	data object ResultScreen: Route

	@Serializable
	data object StatisticsScreen: Route
}