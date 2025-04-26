package com.cericatto.scribbledash.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cericatto.scribbledash.R
import com.cericatto.scribbledash.ui.navigation.Route

@Composable
fun BottomNavigationBar(
	navController: NavController
) {

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

	val navigationItems = listOf(
		NavigationItem(
			title = "Statistics",
			icon = R.drawable.home_bottom_left,
			route = Route.StatisticsScreen,
			tint = Color(0xFFF2852D)
		),
		NavigationItem(
			title = "Home",
			icon = R.drawable.home_bottom_right,
			route = Route.HomeScreen,
			tint = Color(0xFF4B8CFF)
		),
	)

	// Update selected index based on current route
	LaunchedEffect(currentRoute) {
		navigationItems.forEachIndexed { index, item ->
			if (item.route::class.qualifiedName == currentRoute) {
				selectedIndex.intValue = index
			}
		}
	}

	NavigationBar(
		containerColor = Color.White
	) {
		navigationItems.forEachIndexed { index, item ->
			NavigationBarItem(
				selected = selectedIndex.intValue == index,
				onClick = {
					navController.navigate(item.route) {
						// Pop up to the start destination to avoid a large stack
						popUpTo(navController.graph.findStartDestination().id) {
							saveState = true
						}
						// Avoid multiple copies of the same destination.
						launchSingleTop = true
						// Restore state when reselecting a previously selected item.
						restoreState = true
					}
				},
				icon = {
					Icon(
						painter = painterResource(item.icon),
						contentDescription = item.title,
						tint = if (selectedIndex.intValue == index) item.tint else Color.Gray
					)
				},
				label = {
					Text(
						item.title,
						color = if (currentRoute == item.route.toString()) Color.Black else Color.Gray
					)
				},
				colors = NavigationBarItemDefaults.colors(
//					selectedIconColor = MaterialTheme.colorScheme.surface,
					selectedIconColor = Color.White,
//					indicatorColor = MaterialTheme.colorScheme.primary
					indicatorColor = Color.White
				),
			)
		}
	}
}

data class NavigationItem(
	val title: String,
	val icon: Int,
	val route: Route,
	val tint: Color
)