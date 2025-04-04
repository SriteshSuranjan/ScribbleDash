package com.cericatto.scribbledash.ui.difficulty

sealed interface DifficultyScreenAction {
	data object NavigateUp : DifficultyScreenAction
}