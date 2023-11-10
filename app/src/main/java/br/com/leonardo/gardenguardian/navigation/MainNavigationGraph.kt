package br.com.leonardo.gardenguardian.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation


internal const val mainGraphRoute = "main"


fun NavGraphBuilder.mainNavigationGraph() {
    navigation(startDestination = homeRoute, route = mainGraphRoute) {
        homeScreen()
        aboutScreen()
    }
}
fun NavController.navigateToMainGraph(){
    navigate(mainGraphRoute)
}
