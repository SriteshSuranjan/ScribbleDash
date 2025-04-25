package com.cericatto.scribbledash.ui.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.common.ScribbleTitleText
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.drawBackground
import com.cericatto.scribbledash.ui.theme.outfitRegularFont
import com.cericatto.scribbledash.ui.theme.scribbleSubtitleTextColor
import com.cericatto.scribbledash.ui.theme.titleColor
import com.cericatto.scribbledash.ui.utils.contentColor

@Composable
fun StatisticsScreenRoot(
	modifier: Modifier = Modifier,
	onNavigate: (Route) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: StatisticsScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

//	DynamicStatusBarColor()
	StatisticsScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@Composable
private fun StatisticsScreen(
	modifier: Modifier = Modifier,
	onAction: (StatisticsScreenAction) -> Unit,
	state: StatisticsScreenState
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
		StatisticsScreenContent(
			modifier = modifier,
			onAction = onAction,
			state = state
		)
	}
}

@Composable
private fun StatisticsScreenContent(
	modifier: Modifier = Modifier,
	onAction: (StatisticsScreenAction) -> Unit,
	state: StatisticsScreenState
) {
	Column(
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top,
		modifier = modifier
			.background(color = drawBackground)
			.fillMaxSize()
			.padding(10.dp)
	) {
		Text(
			text = "Statistics",
			color = titleColor,
			fontFamily = outfitRegularFont,
			fontWeight = FontWeight.Bold,
			fontSize = 24.sp
		)
		Spacer(
			modifier = Modifier.padding(vertical = 15.dp)
		)
		StatisticsCard(
			drawableId = R.drawable.stat_hourglass,
			statsText = "0%"
		)
		Spacer(
			modifier = Modifier.padding(vertical = 10.dp)
		)
		StatisticsCard(
			drawableId = R.drawable.stat_bolt
		)
	}
}

@Composable
private fun StatisticsCard(
	drawableId: Int,
	statsText: String = "0",
	modifier: Modifier = Modifier
) {
	val text = "Nothing to track... for now"
	Row(
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.shadow(
				elevation = 5.dp,
				shape = RoundedCornerShape(20.dp),
			)
			.background(
				color = Color.White,
				shape = RoundedCornerShape(20.dp)
			)
			.fillMaxWidth()
			.padding(horizontal = 10.dp)
	) {
		Image(
			painter = painterResource(drawableId),
			contentDescription = text,
			modifier = Modifier
				.size(56.dp)
		)
		Text(
			text = text,
			style = TextStyle(
				color = scribbleSubtitleTextColor,
				fontFamily = outfitRegularFont,
			),
			modifier = Modifier
				.weight(1f)
				.padding(start = 10.dp)
		)
		ScribbleTitleText(
			text = statsText,
			fontSize = 48.sp,
			modifier = Modifier
				.padding(end = 10.dp)
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun StatisticsScreenContentPreview() {
	StatisticsScreenContent(
		modifier = Modifier,
		onAction = {},
		state = StatisticsScreenState()
	)
}