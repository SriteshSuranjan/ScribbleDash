package com.cericatto.scribbledash.ui.common

import com.cericatto.scribbledash.ui.navigation.Route

sealed class UiEvent {
	data class Navigate(val route: Route): UiEvent()
	data object NavigateUp: UiEvent()
}