package br.com.leonardo.gardenguardian.ui.activity

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.UUID

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            GardenGuardianTheme {
                Surface {
                    NavHost(navController = navController, startDestination = "home") {
                        composable(route = "home") { HomeScreen() }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}

