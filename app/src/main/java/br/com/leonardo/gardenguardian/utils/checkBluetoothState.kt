package br.com.leonardo.gardenguardian.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

fun checkBluetoothState():Boolean {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    return bluetoothAdapter?.isEnabled ?: false

}

// @SuppressLint("MissingPermission")
// fun isDeviceConnected(deviceName: String): Boolean {
//     val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
//    pairedDevices?.forEach { device ->
//        if (device.name == deviceName) {
//            return true
//        }
//    }
//    return false
//}