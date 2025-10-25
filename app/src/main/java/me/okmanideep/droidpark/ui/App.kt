package me.okmanideep.droidpark.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.okmanideep.droidpark.ui.theme.DroidParkTheme

@Composable
fun DroidParkApp() {
    val navController = rememberNavController()
    DroidParkTheme {
        NavHost(
            navController = navController,
            startDestination = Destinations.HOME
        ) {
            composable(Destinations.HOME) {
                HomeScreen(navController = navController)
            }
            composable(Destinations.DETAILS) { backStackEntry ->
                val dogId = backStackEntry.arguments?.getString("id") ?: "Unknown"
                DetailScreen(dogId, navController = navController)
            }
        }
    }
}