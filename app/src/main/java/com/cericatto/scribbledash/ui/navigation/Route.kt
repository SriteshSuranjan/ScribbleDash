package com.cericatto.scribbledash.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
	@Serializable
	data object LeftScreen: Route

	@Serializable
	data object HomeScreen: Route

	@Serializable
	data object DifficultyScreen: Route
}