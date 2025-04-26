package com.cericatto.scribbledash.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.common.ObserveAsEvents
import com.cericatto.scribbledash.ui.common.ScribbleSubtitleText
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.common.UiEvent
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.bagelFatOneRegularFont
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.theme.homeBackgroundTitleColor
import com.cericatto.scribbledash.ui.theme.homeGreenBorder
import com.cericatto.scribbledash.ui.utils.contentColor

@Composable
fun HomeScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: HomeScreenViewModel = hiltViewModel()
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
	HomeScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun HomeScreen(
	onAction: (HomeScreenAction) -> Unit,
	state: HomeScreenState,
	modifier: Modifier = Modifier
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
		Scaffold(
			modifier = Modifier.fillMaxSize()
		) { _ ->
			HomeScreenContent(
				modifier = modifier,
				onAction = onAction,
				state = state
			)
		}
	}
}

@Composable
private fun HomeScreenContent(
	modifier: Modifier = Modifier,
	onAction: (HomeScreenAction) -> Unit,
	state: HomeScreenState
) {
	Column(
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.background(color = homeBackground)
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
		GameModeCardView(
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun GameModeCardView(
	modifier: Modifier = Modifier,
	onAction: (HomeScreenAction) -> Unit,
	state: HomeScreenState
) {
	Row(
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.clickable {
				onAction(HomeScreenAction.NavigateToDifficulty)
			}
			.padding(top = 40.dp)
			.padding(horizontal = 5.dp)
			.fillMaxWidth()
			.background(
				color = homeGreenBorder,
				shape = RoundedCornerShape(15.dp)
			)
			.padding(10.dp)
			.background(
				color = Color.White,
				shape = RoundedCornerShape(15.dp)
			)
			.clip(RoundedCornerShape(15.dp))
	) {
		Text(
			text = "One Round\nWonder",
			style = TextStyle(
				color = homeBackgroundTitleColor,
				fontFamily = bagelFatOneRegularFont,
				fontSize = 24.sp,
				textAlign = TextAlign.Start
			),
			modifier = Modifier.fillMaxWidth()
				.padding(20.dp)
				.weight(1f)
		)
		HomePenImage()
	}
}

@Composable
private fun RowScope.HomePenImage() {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.weight(1f)
	) {
		Image(
			painter = painterResource(R.drawable.home_draw),
			contentDescription = "Home image",
			modifier = Modifier
				.padding(top = 10.dp)
		)
		Image(
			painter = painterResource(R.drawable.home_pen),
			contentDescription = "Home image",
			modifier = Modifier.align(Alignment.BottomEnd)
		)
	}
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

@Preview(showBackground = true)
@Composable
private fun HomePenImagePreview() {
	Row(
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.padding(40.dp)
			.size(200.dp)
			.background(
				color = Color.Gray.copy(alpha = 0.3f),
				shape = RoundedCornerShape(100.dp)
			)
			.clip(shape = RoundedCornerShape(100.dp))
	) {
		HomePenImage()
	}
}