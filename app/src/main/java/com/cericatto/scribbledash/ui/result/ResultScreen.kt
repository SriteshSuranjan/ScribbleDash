package com.cericatto.scribbledash.ui.result

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ScribbleSubtitleText
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.utils.contentColor

@Composable
fun ResultScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: ResultScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	ObserveAsEvents(viewModel.events) { event ->
		when (event) {
			is UiEvent.NavigateUp -> onNavigateUp()
			is UiEvent.Navigate -> onNavigate(event.route)
			else -> Unit
		}
	}

//	DynamicStatusBarColor()
	ResultScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ResultScreen(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState
) {
	if (state.loading) {
		Box(
			modifier = Modifier
				.padding(vertical = 20.dp)
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(
				color = contentColor(),
				strokeWidth = 4.dp,
				modifier = Modifier.size(64.dp)
			)
		}
	} else {
		ResultScreenContent(
			modifier = modifier,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun ResultScreenContent(
	modifier: Modifier = Modifier,
	onAction: (ResultScreenAction) -> Unit,
	state: ResultScreenState
) {
	Column(
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top,
		modifier = modifier.background(color = homeBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		ScribbleTitleText(
			text = "ScribbleDash",
			fontSize = 30.sp
		)
		ScribbleTitleText(
			text = "Start drawing!",
			modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
		)
		ScribbleSubtitleText(
			text = "Select game mode"
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenContentPreview() {
	ResultScreenContent(
		modifier = Modifier,
		onAction = {},
		state = ResultScreenState()
	)
}