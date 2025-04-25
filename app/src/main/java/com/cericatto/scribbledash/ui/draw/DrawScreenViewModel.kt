package com.cericatto.scribbledash.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cericatto.scribbledash.model.PathData
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject
import kotlin.concurrent.timer

@HiltViewModel
class DrawScreenViewModel @Inject constructor() : ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(DrawScreenState())
	val state: StateFlow<DrawScreenState> = _state.asStateFlow()

	init {
		startCountdown()
		_state.update {
			it.copy(
				drawableId = initDrawableList().random()
			)
		}
	}

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
			is DrawScreenAction.OnNavigateToResult -> navigateToResult()
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
		println("[] ${_state.value.paths}")
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

	private fun onUndoButtonClicked() {
		viewModelScope.launch {
			val currentState = _state.value
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

	private fun startCountdown() {
		viewModelScope.launch {
			for (i in 3 downTo 0) {
				_state.update {
					it.copy(
						timer = i
					)
				}
				delay(1000)
			}
			println("Countdown finished!")
			_state.update {
				it.copy(
					drawMode = true
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

	private fun navigateToResult() {
		viewModelScope.launch {
			_events.send(
				UiEvent.Navigate(
					Route.ResultScreen
				)
			)
		}
	}
}