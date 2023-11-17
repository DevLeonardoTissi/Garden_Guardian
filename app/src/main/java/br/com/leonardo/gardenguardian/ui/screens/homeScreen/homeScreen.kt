package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.provider.Settings
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.ui.ARDUINO_DEVICE_NAME
import br.com.leonardo.gardenguardian.ui.DEFAULT_IMAGE_URL
import br.com.leonardo.gardenguardian.ui.components.AnimatedAlertDialogWithConfirmButton
import br.com.leonardo.gardenguardian.ui.components.DialogWithImage
import br.com.leonardo.gardenguardian.ui.components.ModalBottomSheetWithAnimation
import br.com.leonardo.gardenguardian.ui.components.MyAsyncImage
import br.com.leonardo.gardenguardian.ui.components.NonDismissableAlertDialog
import br.com.leonardo.gardenguardian.ui.components.rememberLegend
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import br.com.leonardo.gardenguardian.ui.theme.dark_yellow
import br.com.leonardo.gardenguardian.ui.theme.md_theme_light_primary
import br.com.leonardo.gardenguardian.ui.theme.md_theme_light_tertiary
import br.com.leonardo.gardenguardian.ui.theme.red
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.enums.BluetoothState
import br.com.leonardo.gardenguardian.utils.enums.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import kotlinx.coroutines.launch
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
    val openAlertDialogBluetoothWasEnable = remember { mutableStateOf(false) }
    val openAlertDialogDeviceNotFound = remember { mutableStateOf(false) }
    val openAlertDialogEditPlantImage = remember { mutableStateOf(false) }
    val openAlertDialogLoad = remember { mutableStateOf(false) }
    val openAlertDialogBluetoothAlreadyActivated = remember { mutableStateOf(false) }
    val openBottomSheetConnectionError = remember { mutableStateOf(false) }
    val openBottomSheetEnableBluetoothFirst = remember { mutableStateOf(false) }


    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {

            if (it.resultCode != Activity.RESULT_OK) {
                openAlertDialogErrorEnableBluetooth.value = true
            } else {
                openAlertDialogBluetoothWasEnable.value = true
            }

        })

    val openBluetoothSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    val bluetoothState by homeScreenViewModel.bluetoothStatus.collectAsStateWithLifecycle(
        initialValue = BluetoothState.DISABLED
    )

    val bluetoothDeviceStatus by homeScreenViewModel.deviceConnectionState.collectAsStateWithLifecycle(
        initialValue = DeviceConnectionState.DISCONNECTED
    )

    val plant by homeScreenViewModel.plant.collectAsStateWithLifecycle(null)
    val settings by homeScreenViewModel.settings.collectAsStateWithLifecycle(initialValue = null)

    val plantState by homeScreenViewModel.plantState.collectAsStateWithLifecycle(initialValue = null)

    val percentage by homeScreenViewModel.moisturePercentage.collectAsStateWithLifecycle(
        initialValue = null
    )


    val dataLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val scrollState = rememberChartScrollState()


    val selectColorByBluetoothStatus by animateColorAsState(
        targetValue = when (bluetoothState) {
            BluetoothState.ENABLED -> md_theme_light_primary
            BluetoothState.DISABLED -> red
        }, label = context.getString(R.string.selectColorByBluetoothStatusLabel)
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

        }, label = context.getString(R.string.selectColorByPlantStateLabel)
    )

    val selectColorByConnectionWithDeviceStatus by animateColorAsState(
        targetValue = when (bluetoothDeviceStatus) {
            DeviceConnectionState.CONNECTED -> md_theme_light_primary
            DeviceConnectionState.DISCONNECTED -> red
        }, label = context.getString(R.string.selectColorByConnectionWithDeviceStatusLabel)
    )

    val coroutineScope = rememberCoroutineScope()

    homeScreenViewModel.checkInitialBluetoothState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(400.dp, 450.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    selectColorByPlantState,
                                    Color.White
                                )
                            )
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
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = context.getString(R.string.iconEditDescription),
                            tint = Color.White
                        )
                    }

                }

                Spacer(modifier = Modifier.height(100.dp))

                val textPresentation = when (bluetoothDeviceStatus) {
                    DeviceConnectionState.DISCONNECTED -> context.getString(R.string.connectionDeviceDisconnectedStateText)
                    DeviceConnectionState.CONNECTED -> context.getString(R.string.connectionDeviceConnectedStateText)
                }

                Text(
                    text = textPresentation,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(all = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = selectColorByConnectionWithDeviceStatus
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
                            contentDescription = context.getString(R.string.iconBluetoothButton),
                            tint = selectColorByBluetoothStatus,
                            modifier = Modifier
                        )

                    }
                    IconButton(onClick = {
                        var foundDevice = false

                        if (bluetoothState == BluetoothState.ENABLED) {
                            if (bluetoothPermissionLauncher.allPermissionsGranted) {

                                coroutineScope.launch {

                                    if (bluetoothDeviceStatus == DeviceConnectionState.CONNECTED) {
                                        BluetoothSocketSingleton.socket?.close()

                                    } else {

                                        openAlertDialogLoad.value = true

                                        val pairedDevices: Set<BluetoothDevice>? =
                                            bluetoothAdapter?.bondedDevices

                                        pairedDevices?.forEach { device ->
                                            val deviceName = device.name

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
                                                    openBottomSheetConnectionError.value = true

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
                            openBottomSheetEnableBluetoothFirst.value = true
                        }

                    }) {
                        Icon(
                            painter = painterResource(
                                id = when (bluetoothDeviceStatus) {
                                    DeviceConnectionState.DISCONNECTED -> R.drawable.ic_link_off
                                    DeviceConnectionState.CONNECTED -> R.drawable.ic_check
                                }
                            ),
                            contentDescription = context.getString(R.string.iconLinkButton),
                            tint = selectColorByConnectionWithDeviceStatus
                        )
                    }
                }
            }
        }


        settings?.showNotification?.let { value ->

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = context.getString(R.string.switchTextNotifications),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = md_theme_light_primary
            )

            Switch(checked = value, onCheckedChange = {
                homeScreenViewModel.updateShowNotificationSetting(it)
            })

            Spacer(modifier = Modifier.height(50.dp))

        }

        percentage?.let {
            dataLineSpec.add(
                LineChart.LineSpec(
                    lineColor = md_theme_light_tertiary.toArgb(),
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White,
                                md_theme_light_tertiary
                            )
                        )
                    )

                )
            )

            Chart(
                modifier = Modifier.padding(16.dp),
                chart = lineChart(lines = dataLineSpec),
                chartModelProducer = ChartEntryModelProducer(it),
                startAxis = rememberStartAxis(
                    titleComponent = textComponent {
                        Text(
                            text = context.getString(R.string.chartTitle),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(all = 8.dp),
                            fontSize = 18.sp,
                            color = md_theme_light_primary
                        )
                    }
                ),
                bottomAxis = rememberBottomAxis(),
                chartScrollState = scrollState,
                legend = rememberLegend()
            )

            Spacer(modifier = Modifier.height(100.dp))

        }

    }



    if (openAlertDialogNotSupportBluetooth.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogNotSupportBluetooth.value = false },
            onDismissRequest = { openAlertDialogNotSupportBluetooth.value = false },
            rawRes = R.raw.sad,
            text = context.getString(R.string.AlertDialogNotSupportBluetoothText),
            title = context.getString(R.string.AlertDialogNotSupportBluetoothTitle)
        )
    }

    if (openAlertDialogErrorEnableBluetooth.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogErrorEnableBluetooth.value = false },
            onDismissRequest = { openAlertDialogErrorEnableBluetooth.value = false },
            rawRes = R.raw.error,
            text = context.getString(R.string.AlertDialogErrorEnableBluetoothText),
            title = context.getString(R.string.AlertDialogErrorEnableBluetoothTitle)
        )
    }

    if (openAlertDialogBluetoothAlreadyActivated.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogBluetoothAlreadyActivated.value = false },
            onDismissRequest = { openAlertDialogBluetoothAlreadyActivated.value = false },
            rawRes = R.raw.bluetooth,
            title = context.getString(R.string.AlertDialogBluetoothAlreadyActivatedTitle),
            text = context.getString(R.string.AlertDialogBluetoothAlreadyActivatedText)
        )
    }


    if (openAlertDialogBluetoothWasEnable.value) {
        AnimatedAlertDialogWithConfirmButton(
            onConfirmation = { openAlertDialogBluetoothWasEnable.value = false },
            onDismissRequest = { openAlertDialogBluetoothWasEnable.value = false },
            rawRes = R.raw.bluetooth_enable,
            text = context.getString(R.string.AlertDialogBluetoothWasEnableText),
            title = context.getString(R.string.AlertDialogBluetoothWasEnableTitle)
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
            text = context.getString(R.string.AlertDialogDeviceNotFoundText, ARDUINO_DEVICE_NAME),
            title = context.getString(R.string.AlertDialogDeviceNotFoundTitle)
        )
    }


    if (openAlertDialogLoad.value) {
        NonDismissableAlertDialog(
            text = context.getString(R.string.alertDialogTryConnectionText),
            animationId = R.raw.bluetooth
        )
    }

    if (openAlertDialogEditPlantImage.value) {
        DialogWithImage(
            onDismissRequest = { openAlertDialogEditPlantImage.value = false },
            onConfirmation = { newUrl ->
                openAlertDialogEditPlantImage.value = false
                homeScreenViewModel.updateImg(newUrl)
            },
            url = plant?.img,
            iconId = R.drawable.ic_grass,
            iconDescription = context.getString(R.string.AlertDialogEditPlantImageIconDescription),
            labelText = context.getString(R.string.AlertDialogEditPlantImagePlaceholder),
            placeholderText = context.getString(R.string.AlertDialogEditPlantImageLabel),
            text = context.getString(R.string.AlertDialogEditPlantImageText),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            context = context
        )
    }

    if (openBottomSheetConnectionError.value) {

        ModalBottomSheetWithAnimation(
            onDismissRequest = { openBottomSheetConnectionError.value = false },
            rawRes = R.raw.error,
            text = context.getString(R.string.BottomSheetErrorConnectionText)
        )
    }

    if (openBottomSheetEnableBluetoothFirst.value) {

        ModalBottomSheetWithAnimation(
            onDismissRequest = { openBottomSheetEnableBluetoothFirst.value = false },
            rawRes = R.raw.bluetooth,
            text = context.getString(R.string.BottomSheetEnableBluetoothFirstText)
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



