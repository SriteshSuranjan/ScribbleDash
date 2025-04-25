package com.cericatto.scribbledash.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.navigation.Route

@Composable
fun BottomNavigationBar(
	navController: NavController
) {
	val selectedNavigationIndex = rememberSaveable {
		mutableIntStateOf(0)
	}

	val navigationItems = listOf(
		NavigationItem(
			title = "Statistics",
			icon = R.drawable.home_bottom_left,
			route = Route.StatisticsScreen
		),
		NavigationItem(
			title = "Home",
			icon = R.drawable.home_bottom_right,
			route = Route.HomeScreen
		),
	)

	NavigationBar(
		containerColor = Color.White
	) {
		navigationItems.forEachIndexed { index, item ->
			NavigationBarItem(
				selected = selectedNavigationIndex.intValue == index,
				onClick = {
					selectedNavigationIndex.intValue = index
					navController.navigate(item.route)
				},
				icon = {
					Icon(
						painter = painterResource(item.icon),
						contentDescription = item.title
					)
				},
				label = {
					Text(
						item.title,
						color = if (index == selectedNavigationIndex.intValue)
							Color.Black
						else Color.Gray
					)
				},
				colors = NavigationBarItemDefaults.colors(
					selectedIconColor = MaterialTheme.colorScheme.surface,
					indicatorColor = MaterialTheme.colorScheme.primary
				)

			)
		}
	}
}

data class NavigationItem(
	val title: String,
	val icon: Int,
	val route: Route
)