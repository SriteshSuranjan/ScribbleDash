package com.cericatto.scribbledash.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cericatto.scribbledash.ui.home.HomeScreenRoot

@Composable
fun NavHostComposable(
	modifier: Modifier = Modifier
) {
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = Route.HomeScreen
	) {
		composable<Route.HomeScreen> {
			HomeScreenRoot(
				modifier = modifier
			)
		}
	}
}