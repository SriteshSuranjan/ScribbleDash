package com.cericatto.scribbledash.ui.home

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.common.DynamicStatusBarColor
import com.cericatto.scribbledash.ui.theme.homeBackground
import com.cericatto.scribbledash.ui.theme.homeBackgroundTitleColor
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
	val customFont = FontFamily(
		Font(R.font.bagel_fat_one_regular, FontWeight.Normal)
	)
	Column(
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top,
		modifier = modifier.background(color = homeBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		Text(
			text = "ScribbleDash",
			style = TextStyle(
				color = homeBackgroundTitleColor,
				fontFamily = customFont,
				fontSize = 30.sp
			)
		)
		Text(
			text = "Start drawing!",
			style = TextStyle(
				color = homeBackgroundTitleColor,
				fontFamily = customFont,
				fontSize = 36.sp,
				textAlign = TextAlign.Center
			),
			modifier = Modifier.fillMaxWidth().padding(top = 80.dp)
		)
		Text(
			text = "Select game mode",
			style = TextStyle(
				color = Color(0xFF807062),
				fontSize = 14.sp,
				textAlign = TextAlign.Center
			),
			modifier = Modifier.fillMaxWidth()
				.padding(top = 5.dp)
		)
		Row(
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(top = 40.dp)
				.padding(horizontal = 5.dp)
				.fillMaxWidth()
				.background(
					color = Color(0xFF0FD180),
					shape = RoundedCornerShape(15.dp)
				)
				.padding(10.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(15.dp)
				)
		) {
			Text(
				text = "One Round\nWonder",
				style = TextStyle(
					color = homeBackgroundTitleColor,
					fontFamily = customFont,
					fontSize = 24.sp,
					textAlign = TextAlign.Start
				),
				modifier = Modifier.fillMaxWidth()
					.padding(20.dp)
					.weight(1f)
			)
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