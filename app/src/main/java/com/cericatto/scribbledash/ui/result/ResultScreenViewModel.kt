package com.cericatto.scribbledash.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cericatto.scribbledash.model.PathData
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.stringToPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ResultScreenViewModel @Inject constructor(): ViewModel() {

	private val _events = Channel<UiEvent>()
	val events = _events.receiveAsFlow()

	private val _state = MutableStateFlow(ResultScreenState())
	val state: StateFlow<ResultScreenState> = _state.asStateFlow()

	fun onAction(action: ResultScreenAction) {
		when (action) {
			ResultScreenAction.NavigateUp -> navigateUp()
			ResultScreenAction.OnTryAgainClicked -> TODO()
		}
	}

	init {
		_state.update { it.copy(loading = false) }
	}

	fun updatePaths(paths: String?) {
		val decodedPaths = try {
			URLDecoder.decode(paths, "UTF-8").stringToPath()
		} catch (e: Exception) {
			println("Path decoding error: ${e.message}")
			emptyList<PathData>()
		}
		_state.update {
			it.copy(paths = decodedPaths)
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