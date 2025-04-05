package com.cericatto.scribbledash.ui.draw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class DrawScreenViewModel @Inject constructor(): ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(DrawScreenState())
	val state: StateFlow<DrawScreenState> = _state.asStateFlow()

	fun onAction(action: DrawScreenAction) {
		when (action) {
			is DrawScreenAction.NavigateUp -> navigateUp()
			is DrawScreenAction.OnUpdateClickedPosition -> TODO()
		}
	}

	init {
		_state.update { it.copy(loading = false) }
	}

	private fun navigateUp() {
		viewModelScope.launch {
			_events.send(
				UiEvent.NavigateUp
			)
		}
	}
}