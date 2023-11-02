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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import br.com.leonardo.gardenguardian.ui.ARDUINO_DEVICE_NAME
import br.com.leonardo.gardenguardian.ui.DEFAULT_IMAGE_URL
import br.com.leonardo.gardenguardian.ui.components.AnimatedAlertDialogWithConfirmButton
import br.com.leonardo.gardenguardian.ui.components.DialogWithImage
import br.com.leonardo.gardenguardian.ui.components.MyAsyncImage
import br.com.leonardo.gardenguardian.ui.components.tryConnectionDeviceAlertDialog
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import br.com.leonardo.gardenguardian.ui.theme.dark_yellow
import br.com.leonardo.gardenguardian.ui.theme.md_theme_light_primary
import br.com.leonardo.gardenguardian.ui.theme.red
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.enums.BluetoothState
import br.com.leonardo.gardenguardian.utils.enums.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {

    val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    val context = LocalContext.current

    val permissions = mutableListOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
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

    val openAlertDialogErrorEnableBluetooth = remember { mutableStateOf(false) }
    val openAlertDialogNotSupportBluetooth = remember { mutableStateOf(false) }
    val openAlertDialogBluetoothEnable = remember { mutableStateOf(false) }
    val openAlertDialogDeviceNotFound = remember { mutableStateOf(false) }
    val openAlertDialogEditPlantImage = remember { mutableStateOf(false) }
    val openAlertDialogLoad = remember { mutableStateOf(false) }
    val openAlertDialogBluetoothAlreadyActivated = remember { mutableStateOf(false) }

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {

            if (it.resultCode != Activity.RESULT_OK) {
                openAlertDialogErrorEnableBluetooth.value = true
            } else {
                openAlertDialogBluetoothEnable.value = true
            }

        })

    val openBluetoothSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    val bluetoothState by homeScreenViewModel.bluetoothStatus.collectAsStateWithLifecycle(
        initialValue = BluetoothState.DISABLED
    )

    val bluetoothDeviceStatus by homeScreenViewModel.deviceConnectionState.collectAsStateWithLifecycle(
        initialValue = DeviceConnectionState.DISCONNECTED
    )

    val plant by homeScreenViewModel.plant.collectAsStateWithLifecycle(null)

    val plantState by homeScreenViewModel.plantState.collectAsStateWithLifecycle(initialValue = null)

    val selectColorByBluetoothStatus by animateColorAsState(
        targetValue = when (bluetoothState) {
            BluetoothState.ENABLED -> md_theme_light_primary
            BluetoothState.DISABLED -> red
        }, label = "Update Color"
    )


    val selectColorByPlantState by animateColorAsState(
        targetValue = when (bluetoothDeviceStatus) {

            DeviceConnectionState.DISCONNECTED -> Color.White
            DeviceConnectionState.CONNECTED -> when (plantState) {
                PlantState.LowWater -> red
                PlantState.Alert -> dark_yellow
                PlantState.Ok -> md_theme_light_primary
                else -> Color.White
            }


        }, label = "Update color"
    )

    val selectColorByDeviceStatus by animateColorAsState(
        targetValue = when (bluetoothDeviceStatus) {
            DeviceConnectionState.CONNECTED -> md_theme_light_primary
            DeviceConnectionState.DISCONNECTED -> red
        }, label = "colors by connection state"
    )

    homeScreenViewModel.checkInitialBluetoothState()


    Surface(
        shape = RoundedCornerShape(15.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(400.dp, 450.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(listOf(selectColorByPlantState, Color.White))
                    )
                    .fillMaxWidth()
            ) {


                MyAsyncImage(
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
                                        selectColorByPlantState
                                    )
                                )
                            ), CircleShape
                        ),
                    model = if (plant?.img.isNullOrBlank()) DEFAULT_IMAGE_URL else plant?.img,
                    description = null,
                    contentScale = ContentScale.Crop,
                )


                IconButton(
                    onClick = { openAlertDialogEditPlantImage.value = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 100.dp, x = 50.dp)
                        .size(50.dp)
                        .background(color = md_theme_light_primary, shape = CircleShape)

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
                            openAlertDialogNotSupportBluetooth.value = true

                        } else if (bluetoothState == BluetoothState.DISABLED) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            enableBluetoothLauncher.launch(enableBtIntent)
                        } else {
                            openAlertDialogBluetoothAlreadyActivated.value = true
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

                        if (bluetoothPermissionLauncher.allPermissionsGranted) {


                            CoroutineScope(Dispatchers.IO).launch {

                                if (bluetoothDeviceStatus == DeviceConnectionState.CONNECTED) {
                                    BluetoothSocketSingleton.socket?.close()
                                } else {

                                    openAlertDialogLoad.value = true

                                    val pairedDevices: Set<BluetoothDevice>? =
                                        bluetoothAdapter?.bondedDevices

                                    pairedDevices?.forEach { device ->
                                        val deviceName = device.name
                                        val deviceHardwareAddress = device.address
                                        Log.i(
                                            "dispositivos",
                                            "nome: $deviceName: - address: $deviceHardwareAddress "
                                        )

                                        if (deviceName == ARDUINO_DEVICE_NAME) {
                                            foundDevice = true

                                            BluetoothSocketSingleton.socket =
                                                device.createRfcommSocketToServiceRecord(
                                                    UUID.fromString(
                                                        "00001101-0000-1000-8000-00805F9B34FB"
                                                    )
                                                )
                                            try {
                                                BluetoothSocketSingleton.socket?.connect()
                                                openAlertDialogLoad.value = false
                                            } catch (e: IOException) {
                                                openAlertDialogLoad.value = false

                                                withContext(Dispatchers.Main) {
                                                    Toast.makeText(
                                                        context,
                                                        "Não foi possível conectar ao arduino",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.i(
                                                        "dispositivos",
                                                        "não conectado ao arduino "
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (!foundDevice) {
                                        openAlertDialogLoad.value = false
                                        openAlertDialogDeviceNotFound.value = true
                                    }

                                }


                            }


                        } else {
                            bluetoothPermissionLauncher.launchMultiplePermissionRequest()
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
                        ), contentDescription = "", tint = selectColorByDeviceStatus
                    )

                }
            }
        }


    }



    if (openAlertDialogNotSupportBluetooth.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogNotSupportBluetooth.value = false },
            onDismissRequest = { openAlertDialogNotSupportBluetooth.value = false },
            rawRes = R.raw.sad,
            text = "Lamentamos, mas seu dispositivo não possui suporte ao bluetooth",
            title = "Dispositivo sem suporte"
        )
    }

    if (openAlertDialogErrorEnableBluetooth.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogErrorEnableBluetooth.value = false },
            onDismissRequest = { openAlertDialogErrorEnableBluetooth.value = false },
            rawRes = R.raw.error,
            text = "Erro ao ativar bluetooth",
            title = "Erro na ativação"
        )
    }

    if (openAlertDialogBluetoothAlreadyActivated.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogBluetoothAlreadyActivated.value = false },
            onDismissRequest = { openAlertDialogBluetoothAlreadyActivated.value = false },
            rawRes = R.raw.bluetooth,
            title = "Bluetooth já ativado",
            text = "Seu dispositivo Bluetooth já está ativado"
        )
    }


    if (openAlertDialogBluetoothEnable.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogBluetoothEnable.value = false },
            onDismissRequest = { openAlertDialogBluetoothEnable.value = false },
            rawRes = R.raw.bluetooth_enable,
            text = "Bluetooth Ativado",
            title = "Sucesso na ativação"
        )
    }

    if (openAlertDialogDeviceNotFound.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = {
                openAlertDialogDeviceNotFound.value = false
                openBluetoothSettingsLauncher.launch(Intent().apply {
                    this.action = Settings.ACTION_BLUETOOTH_SETTINGS
                })
            },
            onDismissRequest = { openAlertDialogDeviceNotFound.value = false },
            rawRes = R.raw.bluetooth_paired,
            text = "Para prosseguir, procure pelo dispositivo $ARDUINO_DEVICE_NAME e faça o pareamento ",
            title = "Dispositivo não pareado"
        )
    }


    if (openAlertDialogLoad.value) {
        tryConnectionDeviceAlertDialog()
    }

    if (openAlertDialogEditPlantImage.value) {
        DialogWithImage(
            onDismissRequest = { openAlertDialogEditPlantImage.value = false },
            onConfirmation = { newUrl ->
                openAlertDialogEditPlantImage.value = false
                homeScreenViewModel.updateImg(newUrl)
            },
            url = plant?.img,
            imageDescription = ""
        )
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






