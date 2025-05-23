package com.cericatto.scribbledash.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.Route
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
class HomeScreenViewModel @Inject constructor(): ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(HomeScreenState())
	val state: StateFlow<HomeScreenState> = _state.asStateFlow()

	fun onAction(action: HomeScreenAction) {
		when (action) {
			is HomeScreenAction.NavigateToDifficulty -> navigateToDifficulty()
		}
	}

	init {
		_state.update { it.copy(loading = false) }
	}

	private fun navigateToDifficulty() {
		viewModelScope.launch {
			_events.send(
				UiEvent.Navigate(
					Route.DifficultyScreen
				)
			)
		}
	}
}