package com.cericatto.scribbledash.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
	@Serializable
	data object HomeScreen: Route
}