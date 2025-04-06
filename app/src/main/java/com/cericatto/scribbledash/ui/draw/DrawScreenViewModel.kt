package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cericatto.scribbledash.model.PathData
import com.cericatto.scribbledash.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawScreenViewModel @Inject constructor() : ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(DrawScreenState())
	val state: StateFlow<DrawScreenState> = _state.asStateFlow()

	fun onAction(action: DrawScreenAction) {
		when (action) {
			is DrawScreenAction.NavigateUp -> navigateUp()
			is DrawScreenAction.OnClearCanvasClick -> onClearCanvasClick()
			is DrawScreenAction.OnDraw -> onDraw(action.offset)
			is DrawScreenAction.OnNewPathStart -> onNewPathStart()
			is DrawScreenAction.OnPathEnd -> onPathEnd()
			is DrawScreenAction.OnSelectColor -> onSelectColor(action.color)
		}
	}

	private fun onSelectColor(color: Color) {
		_state.update {
			it.copy(
				selectedColor = color
			)
		}
	}

	private fun onPathEnd() {
		val currentPathData = state.value.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = null,
				paths = it.paths + currentPathData
			)
		}
	}

	private fun onNewPathStart() {
		_state.update {
			it.copy(
				currentPath = PathData(
					id = System.currentTimeMillis().toString(),
					color = it.selectedColor,
					path = emptyList()
				)
			)
		}
	}

	private fun onDraw(offset: Offset) {
		val currentPath = state.value.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = currentPath.copy(
					path = currentPath.path + offset
				)
			)
		}
		println("paths: ${_state.value.paths}")
	}

	private fun onClearCanvasClick() {
		_state.update {
			it.copy(
				currentPath = null,
				paths = emptyList()
			)
		}
	}

	private fun navigateUp() {
		viewModelScope.launch {
			_events.send(
				UiEvent.NavigateUp
			)
		}
	}
}