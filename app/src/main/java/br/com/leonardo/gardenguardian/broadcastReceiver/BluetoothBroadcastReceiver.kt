package br.com.leonardo.gardenguardian.broadcastReceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.ui.ARDUINO_DEVICE_NAME
import br.com.leonardo.gardenguardian.utils.BluetoothState
import br.com.leonardo.gardenguardian.utils.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.checkBluetoothState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BluetoothBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private val _bluetoothStatus: MutableStateFlow<BluetoothState> =
            MutableStateFlow(BluetoothState.DISABLED)
        val bluetoothStatus: Flow<BluetoothState> = _bluetoothStatus

        private val _deviceConnectionState: MutableStateFlow<DeviceConnectionState> =
            MutableStateFlow(DeviceConnectionState.DISCONNECTED)
        val deviceConnectionState: Flow<DeviceConnectionState> = _deviceConnectionState


        fun checkInitialBluetoothState() {
            if (checkBluetoothState()) {
                _bluetoothStatus.value = BluetoothState.ENABLED
            } else {
                _bluetoothStatus.value = BluetoothState.DISABLED
            }
        }

    }


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action

        action?.let {

            when (it) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device?.name == ARDUINO_DEVICE_NAME) {
                        _deviceConnectionState.value = DeviceConnectionState.CONNECTED
                        startBluetoothPlantMonitorService(context)
                    }
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device?.name == ARDUINO_DEVICE_NAME) {
                        _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
                        stopBluetoothMonitorService(context)
                    }
                }

                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )) {

                        BluetoothAdapter.STATE_OFF -> {
                            _bluetoothStatus.value = BluetoothState.DISABLED
                            stopBluetoothMonitorService(context)
                            _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
                        }

                        BluetoothAdapter.STATE_ON -> _bluetoothStatus.value = BluetoothState.ENABLED
                    }
                }
            }
        }
    }

    private fun startBluetoothPlantMonitorService(context: Context?) {
        val intent =
            Intent(context, BluetoothPlantMonitorService::class.java)
        context?.startService(intent)
    }

    private fun stopBluetoothMonitorService(context: Context?) {
        val intent =
            Intent(context, BluetoothPlantMonitorService::class.java)
        context?.stopService(intent)
    }
}
