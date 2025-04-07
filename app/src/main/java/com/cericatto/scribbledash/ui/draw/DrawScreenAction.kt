package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

sealed interface DrawScreenAction {
	data object NavigateUp : DrawScreenAction
	data object OnNewPathStart: DrawScreenAction
	data class OnDraw(val offset: Offset): DrawScreenAction
	data object OnPathEnd: DrawScreenAction
	data class OnSelectColor(val color: Color): DrawScreenAction
	data object OnClearCanvasClick: DrawScreenAction
	data object OnUndoButtonClicked: DrawScreenAction
	data object OnRedoButtonClicked: DrawScreenAction
}