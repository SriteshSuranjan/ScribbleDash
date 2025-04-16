package com.cericatto.scribbledash.ui.result

sealed interface ResultScreenAction {
	data object NavigateUp : ResultScreenAction
	data object OnTryAgainClicked : ResultScreenAction
}