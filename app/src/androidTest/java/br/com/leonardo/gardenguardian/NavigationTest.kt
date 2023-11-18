package br.com.leonardo.gardenguardian

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.leonardo.gardenguardian.navigation.GardenGuardianNavHost
import br.com.leonardo.gardenguardian.navigation.aboutRoute
import br.com.leonardo.gardenguardian.navigation.homeRoute
import br.com.leonardo.gardenguardian.navigation.navigateToAbout
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            GardenGuardianNavHost(navController = navController)
        }
    }


    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule.onRoot().printToLog("Garden Guardian App")
        composeTestRule
            .onNodeWithContentDescription("pencil icon to inform the possibility of editing")
            .assertIsDisplayed()
    }


    @Test
    fun appNavHost_search_and_click_bluetooth_button() {

        composeTestRule.onNodeWithContentDescription("bluetooth icon to show that, by clicking this button, you will be activating your device bluetooth")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, homeRoute)


    }


    @Test
    fun appNavHost_search_text_after() {
        composeTestRule.onRoot().printToLog("Garden Guardian App")
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithText("Dispositivo Desconectado")
                .fetchSemanticsNodes().size == 1

        }

        composeTestRule.onAllNodesWithText("Dispositivo Desconectado")

    }

    @Test
    fun appNavHost_navigateToAboutScreen() {

        composeTestRule.runOnUiThread {
            navController.navigateToAbout()
        }

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, aboutRoute)
    }


}