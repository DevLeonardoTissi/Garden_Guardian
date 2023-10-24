package br.com.leonardo.gardenguardian.broadcastReceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import br.com.leonardo.gardenguardian.utils.BluetoothState
import br.com.leonardo.gardenguardian.utils.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.PlantState
import br.com.leonardo.gardenguardian.utils.checkBluetoothState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BluetoothBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private val _bluetoothStatus: MutableStateFlow<BluetoothState> =
            MutableStateFlow(BluetoothState.DISABLED)
        val bluetoothStatus: Flow<BluetoothState> = _bluetoothStatus



        fun checkInitialBluetoothState() {
            if (checkBluetoothState()) {
                _bluetoothStatus.value = BluetoothState.ENABLED
            } else {
                _bluetoothStatus.value = BluetoothState.DISABLED
            }
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        val action: String? = intent?.action

        action?.let {

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> _bluetoothStatus.value = BluetoothState.DISABLED
                    BluetoothAdapter.STATE_ON -> _bluetoothStatus.value = BluetoothState.ENABLED

                }

            }


//        intent?.let {
//            if (it.action == BluetoothDevice.ACTION_FOUND){
//                Log.i("TAG", "onReceive: entrou")
//            }else{
//                Log.i("TAG", "onReceive: diferente")
//            }
//        }?: kotlin.run {
//            Log.i("TAG", "onReceive: sem ação")
//        }


//        when(intent?.action) {
//
//
//            BluetoothDevice.ACTION_FOUND -> {
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                val device: BluetoothDevice? =
//                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//
//                device?.let{
//                    bluetoothDeviceList.add(it)
//                    Log.i("TAG", "onReceive: ${bluetoothDeviceList.toString()}")
//                }
//                val deviceName = device?.name
//                val deviceHardwareAddress = device?.address // MAC address
//            }
//        }
        }
    }
}