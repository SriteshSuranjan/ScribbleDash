package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset

sealed interface DrawScreenAction {
	data object NavigateUp : DrawScreenAction
//	data class OnMovementPlayed(val boardPosition: BoardPosition) : DrawScreenAction
	data class OnUpdateClickedPosition(val offsetPosition: Offset) : DrawScreenAction
//	data object OnUndoButtonClicked : DrawScreenAction
//	data class OnUpdateAnimationStatus(val boardPosition: BoardPosition) : DrawScreenAction
}