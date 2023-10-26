package br.com.leonardo.gardenguardian.broadcastReceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
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


//        fun checkInitialConnectionDevice() {
//            if (checkBluetoothState()) {
//                if (isDeviceConnected("HC-06")) {
//                    _deviceConnectionState.value = DeviceConnectionState.CONNECTED
//                } else {
//                    _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
//                }
//            } else {
//                _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
//            }
//        }
    }


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {

        val action: String? = intent?.action

        action?.let {

            when (it) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device?.name == "HC-06") {
                        // Dispositivo conectado
                        Log.i("TAG", "Conectado ao dispositivo HC-06")
                        Toast.makeText(context, "Dispositivo HC-06 conectado", Toast.LENGTH_SHORT)
                            .show()
                        // Adicione aqui a lógica adicional após a conexão bem-sucedida

                        _deviceConnectionState.value = DeviceConnectionState.CONNECTED

                        val intent =
                            Intent(context, BluetoothPlantMonitorService::class.java)
                        context?.startService(intent)

                    }
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device?.name == "HC-06") {
                        // Dispositivo desconectado
                        Log.i("TAG", "Desconectado do dispositivo HC-06")
                        Toast.makeText(
                            context,
                            "Dispositivo HC-06 desconectado",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Adicione aqui a lógica adicional após a desconexão
                        _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED

                        val intent =
                            Intent(context, BluetoothPlantMonitorService::class.java)
                        context?.stopService(intent)
                    }
                }

                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )) {
                        BluetoothAdapter.STATE_OFF -> {
                            _bluetoothStatus.value =
                                BluetoothState.DISABLED

                            _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
                        }

                        BluetoothAdapter.STATE_ON -> _bluetoothStatus.value = BluetoothState.ENABLED
                    }
                }
            }


        }


    }
}
