package br.com.leonardo.gardenguardian.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreen

internal const val homeRoute = "home"
fun NavGraphBuilder.homeScreen() {
    composable(homeRoute) { HomeScreen() }
}

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(homeRoute, navOptions)
}