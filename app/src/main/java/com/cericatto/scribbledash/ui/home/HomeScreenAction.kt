package com.cericatto.scribbledash.ui.home

sealed interface HomeScreenAction {
	data object OnRetry : HomeScreenAction
}