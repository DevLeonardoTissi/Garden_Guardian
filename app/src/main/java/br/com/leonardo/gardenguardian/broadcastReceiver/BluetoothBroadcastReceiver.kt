package br.com.leonardo.gardenguardian.broadcastReceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BluetoothBroadcastReceiver : BroadcastReceiver() {
    private val bluetoothDeviceList: MutableList<BluetoothDevice> = mutableListOf()


    override fun onReceive(context: Context?, intent: Intent?) {

        val action: String? = intent?.action

        action?.let {

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> Toast.makeText(
                        context,
                        "Bluetooth Desativado",
                        Toast.LENGTH_LONG
                    ).show()

                    BluetoothAdapter.STATE_ON -> Toast.makeText(
                        context,
                        "Bluetooth Ativado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE,
                    BluetoothAdapter.ERROR
                )

                when (state) {
                    BluetoothAdapter.STATE_CONNECTED -> Toast.makeText(
                        context,
                        "Dispositivo Bluetooth está conectado",
                        Toast.LENGTH_SHORT
                    ).show()

                    BluetoothAdapter.STATE_DISCONNECTED -> Toast.makeText(
                        context,
                        "Dispositivo Bluetooth está desconectado",
                        Toast.LENGTH_SHORT
                    ).show()

                }


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