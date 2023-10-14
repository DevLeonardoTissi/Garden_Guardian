package br.com.leonardo.gardenguardian.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme

class MainActivity : ComponentActivity() {
    private val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
        BluetoothBroadcastReceiver()

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    private val permissionBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                enableBluetooth()

            } else {
                Toast.makeText(this, "Permissão negada ao bluetooth", Toast.LENGTH_SHORT).show()
            }

        }


    private val permitionBluetoothScanLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

                searchDevices()

            } else {
                Toast.makeText(this, "Permissão negada para scan", Toast.LENGTH_SHORT).show()
            }

        }


    private fun enableBluetooth() {

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetoothLauncher.launch(enableBtIntent)
            }
//            else if (bluetoothAdapter!!.isEnabled) {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                requestBluetoothLauncher.launch(enableBtIntent)
//            }
        }
    }


    private val requestBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            if (hasBluetoothScanPermission()) {
                searchDevices()
            } else {
                permitionBluetoothScanLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
            }


        } else {
            Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
        }

    }


    private fun searchDevices() {
        Log.i("TAG", "ttt: ")
        bluetoothAdapter?.startDiscovery()
        Log.i("TAG", "ttt: passou do startdiscovery")

        Log.i("TAG", "ttt: passou do intentfilter")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(bluetoothBroadcastReceiver, filter)

        askBluetoothConnectPermission()




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

    private fun askBluetoothConnectPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (!hasBluetoothPermission()) {
                permissionBluetoothLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                enableBluetooth()
            }
        } else {
            enableBluetooth()
        }
    }

    private fun hasBluetoothPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun hasBluetoothScanPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver()
    }
}

