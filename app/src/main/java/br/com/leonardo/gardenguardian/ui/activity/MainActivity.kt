package br.com.leonardo.gardenguardian.ui.activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.navigation.GardenGuardianNavHost
import br.com.leonardo.gardenguardian.navigation.aboutRoute
import br.com.leonardo.gardenguardian.navigation.homeRoute
import br.com.leonardo.gardenguardian.navigation.navigateToAbout
import br.com.leonardo.gardenguardian.navigation.navigateToHome
import br.com.leonardo.gardenguardian.ui.SNACK_BAR_KEY
import br.com.leonardo.gardenguardian.ui.screens.home.HomeScreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import br.com.leonardo.gardenguardian.widget.MonitorWidget
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
        BluetoothBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val navController = rememberNavController()
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val visitMessage = backStackEntryState
                ?.savedStateHandle
                ?.getStateFlow<String?>(SNACK_BAR_KEY, null)
                ?.collectAsState()
            val currentDestination = backStackEntryState?.destination
            val scope = rememberCoroutineScope()
            val snackBarHost = remember { SnackbarHostState() }

            visitMessage?.value?.let { message ->
                scope.launch {
                    snackBarHost.showSnackbar(message = message, duration = SnackbarDuration.Short)
                }
            }

            MonitorWidget().updateWidget(this)

            GardenGuardianTheme {
                Surface {

                    val (icon, navigate, contentDescription) = remember(currentDestination) {
                        when (currentDestination?.route) {

                            aboutRoute -> Triple(
                                Icons.Default.ArrowBack, navController::navigateToHome,
                                getString(R.string.FloatingActionButtonIconDescriptionBack)
                            )

                            else -> Triple(
                                Icons.Default.Info, navController::navigateToAbout,
                                getString(R.string.FloatingActionButtonIconDescriptionAbout)
                            )
                        }
                    }

                    Scaffold(floatingActionButton = {
                        FloatingActionButton(onClick = {

                            val navOptions = navOptions {

                                currentDestination?.let {
                                    val route =
                                        if (it.route == aboutRoute) homeRoute else aboutRoute

                                    if (route == aboutRoute) {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            SNACK_BAR_KEY,
                                            getString(R.string.SnackBarMessage)
                                        )
                                    }

                                    popUpTo(route) {
                                        inclusive = true
                                    }
                                }
                            }


                            navigate(navOptions)

                        }) {
                            Icon(icon, contentDescription = contentDescription)
                        }
                    }, snackbarHost = {
                        SnackbarHost(hostState = snackBarHost) { data ->
                            Snackbar(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = data.visuals.message
                                )
                            }

                        }
                    })
                    { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            GardenGuardianNavHost(navController = navController)
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

