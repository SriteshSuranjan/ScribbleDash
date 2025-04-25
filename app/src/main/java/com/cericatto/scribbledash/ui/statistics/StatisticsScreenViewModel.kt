package com.cericatto.scribbledash.ui.statistics

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
class StatisticsScreenViewModel @Inject constructor(): ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(StatisticsScreenState())
	val state: StateFlow<StatisticsScreenState> = _state.asStateFlow()

	fun onAction(action: StatisticsScreenAction) {
		when (action) {
			StatisticsScreenAction.NavigateUp -> navigateUp()
			StatisticsScreenAction.OnTryAgainClicked -> TODO()
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