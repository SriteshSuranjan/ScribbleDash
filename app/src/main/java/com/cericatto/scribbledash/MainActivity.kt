package com.cericatto.scribbledash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cericatto.scribbledash.ui.BottomNavigationBar
import com.cericatto.scribbledash.ui.navigation.NavHostComposable
import com.cericatto.scribbledash.ui.navigation.Route
import com.cericatto.scribbledash.ui.theme.ScribbleDashTheme
import com.cericatto.scribbledash.ui.theme.homeBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		WindowCompat.setDecorFitsSystemWindows(window, false)
		val controller = WindowCompat.getInsetsController(window, window.decorView)
		controller.isAppearanceLightStatusBars = true

		setContent {
			ScribbleDashTheme {
				val navController = rememberNavController()
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					bottomBar = {
						val currentRoute = navController
							.currentBackStackEntryAsState().value?.destination?.route
						val bottomBarRoutes = listOf(
							Route.HomeScreen::class.qualifiedName,
							Route.StatisticsScreen::class.qualifiedName
						)
						if (currentRoute in bottomBarRoutes) {
							BottomNavigationBar(navController = navController)
						}
					}

				) { innerPadding ->
					NavHostComposable(
						navController = navController,
						modifier = Modifier
							.background(homeBackground)
							.padding(innerPadding)
					)
				}
			}
		}
	}
}