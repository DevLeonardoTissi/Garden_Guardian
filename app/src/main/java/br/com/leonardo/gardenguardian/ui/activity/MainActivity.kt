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
    private val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
        BluetoothBroadcastReceiver()

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }


    lateinit var socket: BluetoothSocket
    private var isReadingData = false


    private val bluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                enableBluetooth()
            } else {

            }
        }


    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                bluetoothAdapter?.startDiscovery()

                buscaDispositivosPareados()


            } else {

            }
        }

    private fun buscaDispositivosPareados() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        val arduino = "HC-06"
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            Log.i("dispositivos", "nome: $deviceName: - address: $deviceHardwareAddress ")
            if (deviceName == arduino) {
                connectToDevice(device)
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        }

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

            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )

            bluetoothPermissionLauncher.launch(permissions)

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

    private fun enableBluetooth() {
        // Verifique se o adaptador Bluetooth é nulo e se está ativado
        if (bluetoothAdapter == null) {
            // O dispositivo não suporta Bluetooth, lide com isso adequadamente
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        } else {
            // Bluetooth já está ativado, inicie a descoberta aqui
//            bluetoothAdapter?.startDiscovery()
            buscaDispositivosPareados()
        }


    }

    private fun connectToDevice(device: BluetoothDevice) {
        val uuid: UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID genérico para SPP (Serial Port Profile)
        socket = device.createRfcommSocketToServiceRecord(uuid)


        try {
            socket.connect()
            Log.i("dispositivos", "Conectado ao arduino ")
//            val outputStream = socket.outputStream
//            val message = "c"
//            outputStream.write(message.toByteArray())
            startReadingData()


        } catch (e: IOException) {
            Log.i("dispositivos", "não conectado ao arduino ")
        }
    }


    private fun startReadingData() {
        isReadingData = true
        Thread {
            while (isReadingData) {
                try {
                    val inputStream = socket.inputStream
                    val buffer = ByteArray(1024)
                    val bytes = inputStream.read(buffer) // Lê os dados recebidos
                    val message = String(buffer, 0, bytes)
                    Log.i("dispositivos", message)

                } catch (e: IOException) {
                    // Lide com a exceção adequadamente
                    isReadingData = false
                }
            }


        }.start()
    }

    private fun stopReadingData() {
        isReadingData = false
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothBroadcastReceiver)
    }

}

