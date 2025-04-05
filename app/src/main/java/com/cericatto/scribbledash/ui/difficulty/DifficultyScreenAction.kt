package com.cericatto.scribbledash.ui.difficulty

import com.cericatto.scribbledash.ui.home.HomeScreenAction

sealed interface DifficultyScreenAction {
	data object NavigateUp : DifficultyScreenAction
	data object NavigateToDraw : DifficultyScreenAction
}