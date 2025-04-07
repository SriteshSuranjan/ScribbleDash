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
import kotlin.compareTo

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
			is DrawScreenAction.OnRedoButtonClicked -> onRedoButtonClicked()
			is DrawScreenAction.OnUndoButtonClicked -> onUndoButtonClicked()
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
		println("---------- ON PATH END")
		val currentPathData = state.value.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = null,
				paths = it.paths + currentPathData,
				history = it.history + currentPathData
			)
		}
	}

	private fun onNewPathStart() {
		println("---------- ON NEW PATH START")
		val paths = _state.value.paths
//		val newId = if (paths.isNotEmpty()) {
//			paths[paths.lastIndex].id + 1
//		} else 0

		println("slidingWindows: ${_state.value.slidingWindows}")
		println("history: ${_state.value.history}")
		_state.update {
			it.copy(
				currentPath = PathData(
//					id = System.currentTimeMillis().toString(),
					id = it.count,
					color = it.selectedColor,
					path = emptyList(),
				),
				slidingWindows = emptyList(),
				count = it.count + 1
			)
		}
		println("slidingWindows: ${_state.value.slidingWindows}")
		println("history: ${_state.value.history}")
	}

	private fun onDraw(offset: Offset) {
//		println("---------- ON DRAW")
		val currentPath = state.value.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = currentPath.copy(
					path = currentPath.path + offset
				)
			)
		}
//		println("paths: ${_state.value.paths}")
	}

	private fun onClearCanvasClick() {
		_state.update {
			it.copy(
				currentPath = null,
				paths = emptyList(),
				history = emptyList(),
				slidingWindows = emptyList()
			)
		}
	}

	private fun onUndoButtonClicked() {
		println("---------- UNDO")

		viewModelScope.launch {
			val slidingWindows = _state.value.slidingWindows
			val history = _state.value.history
			var newSlidingWindows : List<Int> = emptyList()
			var newHistory : List<PathData> = emptyList()
			if (slidingWindows.size >= 5) {
				// Remove first item of slidingWindows.
				newSlidingWindows = slidingWindows.slice(1..slidingWindows.size - 1)

				// Add to slidingWindows last index of history.
				val lastIndex = history.size - 1
				newSlidingWindows = newSlidingWindows + lastIndex

				// Remove last item of history.
				newHistory = history.slice(0..lastIndex - 1)
			} else {
				// Add to slidingWindows last index of history.
				val lastIndex = history.size - 1
//				if (lastIndex !in slidingWindows) {
					newSlidingWindows = slidingWindows + lastIndex
//				}

				// Remove last item of history.
				newHistory = history.slice(0..lastIndex - 1)
			}
			// Update state.

			println("slidingWindows: $newSlidingWindows")
			println("history: $newHistory")
			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
		}
	}

	private fun onRedoButtonClicked() {
		println("---------- REDO")

		viewModelScope.launch {
			val slidingWindows = _state.value.slidingWindows
			var newSlidingWindows : List<Int> = emptyList()
			val history = _state.value.history
			val paths = _state.value.paths

			// Remove item of the last index of slidingWindows. This is the index of history.
			val item = slidingWindows.last()
			val slidingWindowsSize = slidingWindows.size
			newSlidingWindows = slidingWindows.slice(0..slidingWindowsSize - 2)
			println("item: $item")

			// Add this item into history.
			var newHistory = history + paths[item]

			// Update state.
			println("slidingWindows: $newSlidingWindows")
			println("history: $newHistory")
			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
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