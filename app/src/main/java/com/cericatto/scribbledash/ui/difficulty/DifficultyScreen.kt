package com.cericatto.scribbledash.ui.difficulty

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.common.CloseScreenIcon
import com.cericatto.scribbledash.ui.common.DynamicStatusBarColor
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ScribbleSubtitleText
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.utils.contentColor

@Composable
fun DifficultyScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: DifficultyScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	ObserveAsEvents(viewModel.events) { event ->
		when (event) {
			is UiEvent.NavigateUp -> onNavigateUp()
			is UiEvent.Navigate -> onNavigate(event.route)
			else -> Unit
		}
	}

	DynamicStatusBarColor()
	DifficultyScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@Composable
private fun DifficultyScreen(
	modifier: Modifier = Modifier,
	onAction: (DifficultyScreenAction) -> Unit,
	state: DifficultyScreenState
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
		DifficultyScreenContent(
			modifier = modifier,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun DifficultyScreenContent(
	modifier: Modifier = Modifier,
	onAction: (DifficultyScreenAction) -> Unit,
	state: DifficultyScreenState
) {
	Column(
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.Top,
		modifier = modifier.background(color = homeBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		CloseScreenIcon(
			onAction = onAction
		)
		ScribbleTitleText(
			text = "Start drawing!",
			modifier = Modifier.fillMaxWidth().padding(top = 100.dp)
		)
		ScribbleSubtitleText(
			text = "Choose a difficulty setting"
		)
		DifficultyRow()
	}
}

@Composable
private fun DifficultyRow() {
	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth()
			.padding(top = 80.dp)
	) {
		DifficultyRowItem(
			difficulty = "Beginner",
			drawableId = R.drawable.difficulty_beginner,
			modifier = Modifier.weight(1f)
		)
		DifficultyRowItem(
			difficulty = "Challenging",
			drawableId = R.drawable.difficulty_challenging,
			modifier = Modifier.padding(bottom = 40.dp)
				.weight(1f)
		)
		DifficultyRowItem(
			difficulty = "Master",
			drawableId = R.drawable.difficulty_master,
			modifier = Modifier.weight(1f)
		)
	}
}

@Composable
private fun DifficultyRowItem(
	modifier: Modifier = Modifier,
	difficulty: String,
	drawableId: Int
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.background(
					color = Color.White,
					shape = RoundedCornerShape(50.dp)
				)
		) {
			Image(
				painter = painterResource(drawableId),
				contentDescription = "Home image",
			)
		}
		ScribbleSubtitleText(
			text = difficulty,
			fontWeight = FontWeight.Medium,
			modifier = Modifier.fillMaxWidth()
				.padding(top = 10.dp)
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun DifficultyScreenContentPreview() {
	DifficultyScreenContent(
		modifier = Modifier,
		onAction = {},
		state = DifficultyScreenState()
	)
}