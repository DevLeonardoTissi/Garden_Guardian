package br.com.leonardo.gardenguardian.utils.sealed

sealed class AppDestinations(val route: String) {

    data object Home : AppDestinations(route = "home")
    data object About : AppDestinations(route = "about")

}
