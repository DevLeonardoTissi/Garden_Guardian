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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme

class MainActivity : ComponentActivity() {

    private val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
        BluetoothBroadcastReceiver()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            GardenGuardianTheme {
                Surface {
                    Scaffold(floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Info, contentDescription = "")
                        }
                    }) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)){
                            NavHost(navController = navController, startDestination = "home") {
                                composable(route = "home") { HomeScreen() }
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
fun AppPreview(){
    GardenGuardianTheme {
        Surface {
            HomeScreen()
        }
    }
}

