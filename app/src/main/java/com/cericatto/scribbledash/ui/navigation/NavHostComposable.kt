package com.cericatto.scribbledash.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cericatto.scribbledash.ui.difficulty.DifficultyScreenRoot
import com.cericatto.scribbledash.ui.draw.DrawScreenRoot
import com.cericatto.scribbledash.ui.home.HomeScreenRoot
import com.cericatto.scribbledash.ui.result.ResultScreenRoot
import com.cericatto.scribbledash.ui.statistics.StatisticsScreenRoot

@Composable
fun NavHostComposable(
	navController: NavHostController,
	modifier: Modifier = Modifier
) {
	NavHost(
		navController = navController,
		startDestination = Route.HomeScreen,
//		modifier = modifier
	) {
		composable<Route.HomeScreen> {
			HomeScreenRoot(
				onNavigate = { navController.navigate(it) },
				onNavigateUp = { navController.navigateUp() },
				modifier = modifier
			)
		}
		composable<Route.DifficultyScreen> {
			DifficultyScreenRoot(
				onNavigate = { navController.navigate(it) },
				onNavigateUp = { navController.navigateUp() },
				modifier = modifier
			)
		}
		composable<Route.DrawScreen> {
			DrawScreenRoot(
				onNavigate = { navController.navigate(it) },
				onNavigateUp = { navController.navigateUp() },
				modifier = modifier
			)
		}
		composable<Route.ResultScreen> { backStackEntry ->
			val args = backStackEntry.toRoute<Route.ResultScreen>()
			ResultScreenRoot(
				onNavigate = { navController.navigate(it) },
				onNavigateUp = { navController.navigateUp() },
				paths = args.paths,
				modifier = modifier
			)
		}

		composable<Route.StatisticsScreen> {
			StatisticsScreenRoot(
				modifier = modifier
			)
		}
	}
}