package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.ui.theme.DarkGreen
import br.com.leonardo.gardenguardian.ui.theme.Red
import br.com.leonardo.gardenguardian.ui.theme.Yellow
import br.com.leonardo.gardenguardian.utils.PlantState
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.util.UUID

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen( homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {

//    val bluetoothBroadcastReceiver: BluetoothBroadcastReceiver =
//        BluetoothBroadcastReceiver()
//
//    val bluetoothAdapter: BluetoothAdapter? by lazy {
//        BluetoothAdapter.getDefaultAdapter()
//    }
//
//
//
//    lateinit var socket: BluetoothSocket
//
//    val startReadingData: ( socket: BluetoothSocket) -> Unit =  {
//        var isReadingData = true
//        Thread {
//            while (isReadingData) {
//                try {
//                    val inputStream = socket.inputStream
//                    val buffer = ByteArray(1024)
//                    val bytes = inputStream.read(buffer) // Lê os dados recebidos
//                    val message = String(buffer, 0, bytes)
//                    Log.i("dispositivos", message)
//
//                } catch (e: IOException) {
//                    // Lide com a exceção adequadamente
//                    isReadingData = false
//                }
//            }
//
//
//        }.start()
//    }
//
//    val connectToDevice: (device: BluetoothDevice, socket: BluetoothSocket)  -> Unit = { device: BluetoothDevice, bluetoothSocket: BluetoothSocket ->
//        val uuid: UUID =
//            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID genérico para SPP (Serial Port Profile)
//        socket = device.createRfcommSocketToServiceRecord(uuid)
//
//
//        try {
//            socket.connect()
//            Log.i("dispositivos", "Conectado ao arduino ")
////            val outputStream = socket.outputStream
////            val message = "c"
////            outputStream.write(message.toByteArray())
//            startReadingData( socket)
//
//
//        } catch (e: IOException) {
//            Log.i("dispositivos", "não conectado ao arduino ")
//        }
//    }
//
//
//    val buscaDispositivosPareados: (bluetoothAdapter: BluetoothAdapter?, socket: BluetoothSocket) -> Unit = { bluetoothAdapter: BluetoothAdapter?, bluetoothSocket: BluetoothSocket ->
//        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
//        val arduino = "HC-06"
//        pairedDevices?.forEach { device ->
//            val deviceName = device.name
//            val deviceHardwareAddress = device.address // MAC address
//            Log.i("dispositivos", "nome: $deviceName: - address: $deviceHardwareAddress ")
//            if (deviceName == arduino) {
//                connectToDevice(device, socket )
//            }
//
//        }
//    }
//
//
//
//
//
//    val permissions = mutableListOf(
//        Manifest.permission.BLUETOOTH,
//        Manifest.permission.BLUETOOTH_ADMIN,
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//
//        )
//
//    if (Build.VERSION.SDK_INT >= 31) {
//        permissions.addAll(
//            listOf(
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT
//            )
//        )
//    }
//
//    val bluetoothPermissionLauncher = rememberMultiplePermissionsState(
//        permissions = permissions
//    )
//
//    val enableBluetoothLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult() , onResult = {
//
//        if (it.resultCode == Activity.RESULT_OK){
//            buscaDispositivosPareados(bluetoothAdapter,socket)
//        }else{
//
//        }
//
//    })
//
//    val enableBluetooth:() -> Unit = {
//        if (bluetoothPermissionLauncher.allPermissionsGranted){
//            if (bluetoothAdapter == null) {
//                // O dispositivo não suporta Bluetooth, lide com isso adequadamente
//
//            }else if (!bluetoothAdapter!!.isEnabled) {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                enableBluetoothLauncher.launch(enableBtIntent)
//            } else {
//                // Bluetooth já está ativado, inicie a descoberta aqui
////            bluetoothAdapter?.startDiscovery()
//                buscaDispositivosPareados(bluetoothAdapter, socket)
//            }
//
//
//        }
//    }
//
//
//
//    //ACTIVITY
//
//    val filter = IntentFilter().apply {
//        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
//        addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
//    }
//
//    LocalContext.current.registerReceiver(bluetoothBroadcastReceiver, filter)
//
//    LaunchedEffect(key1 = Unit){
//        bluetoothPermissionLauncher.launchMultiplePermissionRequest()
//
//
//
//
//
//
//
//    }
//
//    DisposableEffect(bluetoothPermissionLauncher ){
//        onDispose {
//            when{
//                bluetoothPermissionLauncher.allPermissionsGranted -> enableBluetooth()
//            }
//        }
//    }
//
//
//

    val plantState by homeScreenViewModel.plantState.collectAsStateWithLifecycle(initialValue = null)
    val selectColorByState by animateColorAsState(
        targetValue = when (plantState) {

            PlantState.LowWater -> Red
            PlantState.Alert -> Yellow
            else -> DarkGreen
        }, label = "Update color"
    )

    homeScreenViewModel.updateColor()


    Surface(
        shape = RoundedCornerShape(15.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(400.dp, 450.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(listOf(selectColorByState, Color.White))
                    )
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = 100.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.BottomCenter)
                        .border(
                            BorderStroke(
                                2.dp,
                                brush = Brush.verticalGradient(listOf(Color.White,selectColorByState))
                            ), CircleShape
                        ),
                    model = "https://img.freepik.com/fotos-gratis/planta-zz-em-um-vaso-cinza_53876-134285.jpg?w=740&t=st=1696877614~exp=1696878214~hmac=6df009433f3cecc5f802bf4e378b07d68e6374cdbdb12a65f54cb71c89508556",
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentScale = ContentScale.Crop

                )
            }


            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = LoremIpsum(50).values.first(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 8.dp)
            )

        }
    }







}







//private fun stopReadingData() {
//    isReadingData = false
//}



