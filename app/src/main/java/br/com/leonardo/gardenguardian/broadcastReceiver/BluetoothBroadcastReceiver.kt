package br.com.leonardo.gardenguardian.broadcastReceiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothBroadcastReceiver: BroadcastReceiver() {
    private val bluetoothDeviceList: MutableList<BluetoothDevice> = mutableListOf()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                device?.let{
                    bluetoothDeviceList.add(it)
                    Log.i("TAG", "onReceive: ${bluetoothDeviceList.toString()}")
                }
                val deviceName = device?.name
                val deviceHardwareAddress = device?.address // MAC address
            }
        }
    }
}