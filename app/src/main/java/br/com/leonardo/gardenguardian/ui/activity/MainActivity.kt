package br.com.leonardo.gardenguardian.ui.activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.ui.screens.about.AboutHomeScreen
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import br.com.leonardo.gardenguardian.utils.sealed.AppDestinations

class MainActivity : ComponentActivity() {

    private val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
        BluetoothBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination

            GardenGuardianTheme {
                Surface {

                    val (icon, destination, contentDescription) = remember(currentDestination) {
                        when (currentDestination?.route) {
                            AppDestinations.Home.route -> Triple(
                                Icons.Default.Info, AppDestinations.About.route, getString(
                                    R.string.FloatingActionButtonIconDescriptionAbout
                                )
                            )

                            AppDestinations.About.route -> Triple(
                                Icons.Default.ArrowBack,
                                AppDestinations.Home.route,
                                getString(R.string.FloatingActionButtonIconDescriptionBack)
                            )

                            else -> Triple(
                                Icons.Default.Info,
                                AppDestinations.About.route,
                                getString(R.string.FloatingActionButtonIconDescriptionAbout)
                            )
                        }
                    }

                    Scaffold(floatingActionButton = {
                        FloatingActionButton(onClick = {
                            navController.navigate(destination) {
                                popUpTo(destination) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(icon, contentDescription = contentDescription)
                        }
                    })
                    { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            NavHost(
                                navController = navController,
                                startDestination = AppDestinations.Home.route
                            ) {
                                composable(AppDestinations.Home.route) { HomeScreen() }
                                composable(AppDestinations.About.route) { AboutHomeScreen() }
                            }
                        }
                    }
                }
            }
        }


        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }

        registerReceiver(bluetoothBroadcastReceiver, filter)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothBroadcastReceiver)
    }

}

@Preview
@Composable
fun AppPreview() {
    GardenGuardianTheme {
        Surface {
            HomeScreen()
        }
    }
}

