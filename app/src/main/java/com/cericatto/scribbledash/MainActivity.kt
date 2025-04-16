package com.cericatto.scribbledash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cericatto.scribbledash.ui.navigation.NavHostComposable
import com.cericatto.scribbledash.ui.theme.ScribbleDashTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			ScribbleDashTheme {
//				val navController = rememberNavController()
				Scaffold(
//					bottomBar = {
//						BottomNavigationBar(navController)
//					},
					modifier = Modifier.fillMaxSize()
				) { innerPadding ->
					NavHostComposable(
						modifier = Modifier.padding(innerPadding)
					)
				}
			}
		}
	}
}