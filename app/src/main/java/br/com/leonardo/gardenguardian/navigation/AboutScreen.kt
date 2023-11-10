package br.com.leonardo.gardenguardian.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.leonardo.gardenguardian.ui.screens.about.AboutHomeScreen

internal const val aboutRoute = "about"
fun NavGraphBuilder.aboutScreen() {
    composable(aboutRoute) { AboutHomeScreen() }
}

fun NavController.navigateToAbout(navOptions: NavOptions? = null) {
    navigate(aboutRoute, navOptions)
}