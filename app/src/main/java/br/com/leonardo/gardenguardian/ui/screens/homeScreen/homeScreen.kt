package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.ui.DEFAULT_IMAGE_URL
import br.com.leonardo.gardenguardian.ui.theme.DarkGreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import br.com.leonardo.gardenguardian.ui.theme.Red
import br.com.leonardo.gardenguardian.ui.theme.Yellow
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.BluetoothState
import br.com.leonardo.gardenguardian.utils.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.PlantState
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import okhttp3.internal.wait
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {

    val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    val context = LocalContext.current

    homeScreenViewModel.deviceConnected()

    val permissions = mutableListOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,

        )

    if (Build.VERSION.SDK_INT >= 31) {
        permissions.addAll(
            listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    }

    val bluetoothPermissionLauncher = rememberMultiplePermissionsState(
        permissions = permissions
    )

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {

            if (it.resultCode != Activity.RESULT_OK) {
                Log.i("TAG", "HomeScreen: Erro ao ativar bluetooth")
            } else {

            }

        })

    val openBluetoothSettings = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }


    homeScreenViewModel.checkInitialBluetoothState()

    val bluetoothState by homeScreenViewModel.bluetoothStatus.collectAsStateWithLifecycle(
        initialValue = BluetoothState.DISABLED
    )

    val selectColorByBluetoothStatus by animateColorAsState(
        targetValue = when (bluetoothState) {
            BluetoothState.ENABLED -> DarkGreen
            BluetoothState.DISABLED -> Red
        }, label = "Update Color"
    )

    val bluetoothDeviceStatus by homeScreenViewModel.deviceConnectionState.collectAsStateWithLifecycle(
        initialValue = DeviceConnectionState.DISCONNECTED
    )

    val plant by homeScreenViewModel.plant.collectAsStateWithLifecycle(null)


    val plantState by homeScreenViewModel.plantState.collectAsStateWithLifecycle(initialValue = null)
    val selectColorByState by animateColorAsState(
        targetValue = when (bluetoothDeviceStatus) {

            DeviceConnectionState.DISCONNECTED -> Color.White
            DeviceConnectionState.CONNECTED -> when (plantState) {
                PlantState.LowWater -> Red
                PlantState.Alert -> Yellow
                PlantState.Ok -> DarkGreen
                else -> Color.White
            }


        }, label = "Update color"
    )

//    val selectColorByState by animateColorAsState(
//        targetValue = Red,
//        label = "Update color"
//    )


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
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.White,
                                        selectColorByState
                                    )
                                )
                            ), CircleShape
                        ),
                    model = if (plant?.img.isNullOrBlank()) DEFAULT_IMAGE_URL else plant?.img,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentScale = ContentScale.Crop

                )


                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 100.dp, x = 50.dp)
                        .size(50.dp)
                        .background(color = DarkGreen, shape = CircleShape)

                        .border(
                            BorderStroke(
                                2.dp,
                                color = Color.White
                            ), CircleShape
                        )
                ) {
                    Icon(Icons.Default.Edit, "", tint = Color.White)

                }


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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = {


                    if (bluetoothPermissionLauncher.allPermissionsGranted) {
                        if (bluetoothAdapter == null) {
                            // O dispositivo não suporta Bluetooth, lide com isso adequadamente

                        } else if (bluetoothState == BluetoothState.DISABLED) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            enableBluetoothLauncher.launch(enableBtIntent)
                        }

                    } else {
                        bluetoothPermissionLauncher.launchMultiplePermissionRequest()
                    }


                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bluetooth),
                        contentDescription = "",
                        tint = selectColorByBluetoothStatus,
                        modifier = Modifier
                    )

                }
                IconButton(onClick = {
                    var foundDevice = false
                    if (bluetoothState == BluetoothState.ENABLED) {
                        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                        val arduino = "HC-06"
                        Log.i("TAG", "HomeScreen: entrou")

                        pairedDevices?.forEach { device ->
                            val deviceName = device.name
                            val deviceHardwareAddress = device.address
                            Log.i(
                                "dispositivos",
                                "nome: $deviceName: - address: $deviceHardwareAddress "
                            )

                            if (deviceName == arduino) {
                                foundDevice = true
                                val uuid: UUID =
                                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                                BluetoothSocketSingleton.socket =
                                    device.createRfcommSocketToServiceRecord(uuid)
                                try {
                                    BluetoothSocketSingleton.socket?.connect()
                                    Log.i("dispositivos", "Conectado ao arduino ")

                                    homeScreenViewModel.deviceConnected()

                                    val intent =
                                        Intent(context, BluetoothPlantMonitorService::class.java)
                                    context.startService(intent)


                                } catch (e: IOException) {
                                    homeScreenViewModel.deviceDisconnected()
                                    Toast.makeText(
                                        context,
                                        "Não foi possível conectar ao arduino",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.i("dispositivos", "não conectado ao arduino ")
                                }
                            }
                        }

                        if (!foundDevice) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_BLUETOOTH_SETTINGS
                            openBluetoothSettings.launch(intent)

                            Log.i("dispositivos", "Dispositivo hc-06 não encontrado.")
                        }


                    } else {
                        Toast.makeText(
                            context,
                            "Necessário ligar o bluetooth primeiro para tentar conectar ao dispositivo",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                }) {
                    Icon(
                        painter = painterResource(
                            id = when (bluetoothDeviceStatus) {
                                DeviceConnectionState.DISCONNECTED -> R.drawable.ic_link_off
                                DeviceConnectionState.CONNECTED -> R.drawable.ic_check
                            }
                        ), contentDescription = "", tint = DarkGreen
                    )

                }
            }

        }


    }


}


@Preview
@Composable
fun HomeScreenPreview() {
    GardenGuardianTheme {
        Surface {
            HomeScreen()
        }
    }
}


//private fun stopReadingData() {
//    isReadingData = false
//}





