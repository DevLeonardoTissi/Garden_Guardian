package br.com.leonardo.gardenguardian.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun GardenGuardianNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = mainGraphRoute
    ) {
        mainNavigationGraph()
    }
}







