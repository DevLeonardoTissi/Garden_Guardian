package br.com.leonardo.gardenguardian.utils

import android.bluetooth.BluetoothAdapter

fun checkBluetoothState():Boolean {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    return bluetoothAdapter?.isEnabled ?: false

}

