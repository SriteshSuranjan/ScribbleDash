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
import java.util.Stack
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

	/*
	private fun onPathEnd() {
		val state = _state.value
		val currentPathData = state.currentPath ?: return
		_state.update {
			it.copy(
				currentPath = null,
				paths = it.paths + currentPathData,
				history = it.history + currentPathData
			)
		}
	}
	 */

	private fun onPathEnd() {
		viewModelScope.launch {
			val state = _state.value
			val currentPathData = state.currentPath ?: return@launch
			state.currentPath?.let { completedPath ->
				_state.update {
					it.copy(
						paths = it.paths + currentPathData,
						actionStack = Stack(), // Clear stack on new action
						currentPath = null
					)
				}
			}
		}
	}

	private fun onNewPathStart() {
		val state = _state.value
		val newCount = state.count + 1

		_state.update {
			it.copy(
				currentPath = PathData(
					id = newCount,
					color = it.selectedColor,
					path = emptyList(),
				),
				actionStack = Stack(), // Clear stack on new path drawn
				count = newCount
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
	}

	private fun onClearCanvasClick() {
		_state.update {
			it.copy(
				currentPath = null,
				paths = emptyList(),
				actionStack = Stack(), // Clear stack on Canvas cleared
				count = -1
			)
		}
	}

	/*
	private fun onUndoButtonClicked() {
		var newSlidingWindows : List<Int> = emptyList()
		var newHistory : List<PathData> = emptyList()
		val state = _state.value
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
				// Add to slidingWindows last index of history.
				val lastItem = state.history.last()
				newSlidingWindows = state.slidingWindows + lastItem.id

				// Remove last item of history.
				newHistory = state.history.slice(0..state.history.size - 2)
			}

			// Update state.
			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
		}
	}
	 */

	/*
	private fun onUndoButtonClicked() {
		val currentState = _state.value
		if (currentState.history.size <= 1) return // Nothing to undo if history is empty or has 1 item

		viewModelScope.launch {
			val lastItem = currentState.history.last()
			val newHistory = currentState.history.dropLast(1)
			val newSlidingWindows = when {
				currentState.slidingWindows.size >= HISTORY_LIMIT -> {
					currentState.slidingWindows.drop(1) + lastItem.id
				}
				else -> currentState.slidingWindows + lastItem.id
			}

			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
		}
	}
	 */

	private fun onUndoButtonClicked() {
		viewModelScope.launch {
			val currentState = _state.value
//			if (currentState.paths.isEmpty() || currentState.actionStack.isEmpty()) {
			if (currentState.paths.isEmpty()) {
				return@launch
			}

			val pathToRemove = currentState.paths.last()
			val newPaths = currentState.paths.dropLast(1)
			val newActionStack = Stack<PathData>().apply {
				addAll(currentState.actionStack)
				if (size >= HISTORY_LIMIT) removeAt(0) // Remove oldest if at limit.
				push(pathToRemove)
			}

			_state.update {
				it.copy(
					paths = newPaths,
					actionStack = newActionStack
				)
			}
		}
	}

	/*
	private fun onRedoButtonClicked() {
		var newSlidingWindows : List<Int> = emptyList()
		val state = _state.value
		viewModelScope.launch {
			// Remove item of the last index of slidingWindows. This is the index of history.
			val item = state.slidingWindows.last()
			val slidingWindowsSize = state.slidingWindows.size
			newSlidingWindows = state.slidingWindows.slice(0..slidingWindowsSize - 2)

			// Add this item into history.
			var newHistory = state.history + state.paths[item]

			// Update state.
			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
		}
	}
	 */

	/*
	private fun onRedoButtonClicked() {
		val currentState = _state.value
		if (currentState.slidingWindows.isEmpty()) return // Nothing to redo

		viewModelScope.launch {
			val lastIndex = currentState.slidingWindows.last()
			val newSlidingWindows = currentState.slidingWindows.dropLast(1)
			val newHistory = currentState.history + currentState.paths[lastIndex]
			_state.update {
				it.copy(
					slidingWindows = newSlidingWindows,
					history = newHistory
				)
			}
		}
	}
	 */

	private fun onRedoButtonClicked() {
		viewModelScope.launch {
			val currentState = _state.value
			if (currentState.actionStack.isEmpty()) {
				return@launch
			}

			val pathToRedo = currentState.actionStack.pop()
			val newPaths = currentState.paths + pathToRedo

			_state.update {
				it.copy(
					paths = newPaths,
					actionStack = currentState.actionStack
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