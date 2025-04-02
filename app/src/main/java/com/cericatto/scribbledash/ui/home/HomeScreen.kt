package com.cericatto.scribbledash.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.ui.common.DynamicStatusBarColor
import com.cericatto.scribbledash.ui.utils.contentColor

@Composable
fun HomeScreenRoot(
	modifier: Modifier = Modifier,
	viewModel: HomeScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	DynamicStatusBarColor()
	HomeScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@Composable
private fun HomeScreen(
	modifier: Modifier = Modifier,
	onAction: (HomeScreenAction) -> Unit,
	state: HomeScreenState
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
		HomeScreenContent(
			modifier = modifier,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun HomeScreenContent(
	modifier: Modifier = Modifier,
	onAction: (HomeScreenAction) -> Unit,
	state: HomeScreenState
) {
	Column {}
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
	HomeScreenContent(
		modifier = Modifier,
		onAction = {},
		state = HomeScreenState()
	)
}