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

		val state = _state.value
		val currentPathData = state.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = null,
				paths = it.paths + currentPathData,
				history = it.history + currentPathData
			)
		}
		println("slidingWindows: ${state.slidingWindows}")
		println("history: ${state.history.map { it.id }}")
		println("paths: ${state.paths.map { it.id }}")
	}

	private fun onNewPathStart() {
		println("---------- NEW PATH START")
		val state = _state.value
		val newCount = state.count + 1

		_state.update {
			it.copy(
				currentPath = PathData(
					id = newCount,
					color = it.selectedColor,
					path = emptyList(),
				),
				slidingWindows = emptyList(),
				count = newCount
			)
		}

		println("id: ${_state.value.currentPath?.id}")
		println("count: $newCount")
		println("slidingWindows: ${state.slidingWindows}")
		println("history: ${state.history.map { it.id }}")
		println("paths: ${state.paths.map { it.id }}")
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
	}

	private fun onClearCanvasClick() {
		_state.update {
			it.copy(
				currentPath = null,
				paths = emptyList(),
				history = emptyList(),
				slidingWindows = emptyList(),
				count = -1
			)
		}
	}

	private fun onUndoButtonClicked() {
		println("---------- UNDO")

		var newSlidingWindows : List<Int> = emptyList()
		var newHistory : List<PathData> = emptyList()
		val state = _state.value

		println("slidingWindows: ${state.slidingWindows}")
		println("history: ${state.history.map { it.id }}")
		println("paths: ${state.paths.map { it.id }}")
		println("----->>>")

		viewModelScope.launch {
			if (state.slidingWindows.size >= HISTORY_LIMIT) {
				// Remove first item of slidingWindows.
				newSlidingWindows = state.slidingWindows.slice(1..state.slidingWindows.size - 1)

				// Add to slidingWindows last index of history.
				val lastItem = state.history.last()
				newSlidingWindows = newSlidingWindows + lastItem.id

				// Remove last item of history.
				newHistory = state.history.slice(0..state.history.size - 2)
			} else {
				println(">>> slidingWindows.size < 5")

				// Add to slidingWindows last index of history.
				val lastItem = state.history.last()
				newSlidingWindows = state.slidingWindows + lastItem.id

				println(">>> lastItem.id: ${lastItem.id}")

				// Remove last item of history.
				newHistory = state.history.slice(0..state.history.size - 2)
			}

			// Update state.
			println("slidingWindows: $newSlidingWindows")
			println("history: ${newHistory.map { it.id }}")
			println("paths: ${state.paths.map { it.id }}")
			println("<<<-----")
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

		var newSlidingWindows : List<Int> = emptyList()
		val state = _state.value
		println("slidingWindows: ${state.slidingWindows}")
		println("history: ${state.history.map { it.id }}")
		println("paths: ${state.paths.map { it.id }}")
		println("----->>>")

		viewModelScope.launch {
			// Remove item of the last index of slidingWindows. This is the index of history.
			val item = state.slidingWindows.last()
			val slidingWindowsSize = state.slidingWindows.size
			newSlidingWindows = state.slidingWindows.slice(0..slidingWindowsSize - 2)
			println("item: $item")

			// Add this item into history.
			var newHistory = state.history + state.paths[item]

			// Update state.
			println("slidingWindows: $newSlidingWindows")
			println("history: ${newHistory.map { it.id }}")
			println("paths: ${state.paths.map { it.id }}")
			println("<<<-----")
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