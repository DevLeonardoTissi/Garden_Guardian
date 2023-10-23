package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.utils.BluetoothState
import br.com.leonardo.gardenguardian.utils.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.PlantState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val _plantState: MutableStateFlow<PlantState?> = MutableStateFlow(null)
    val plantState: Flow<PlantState?> = _plantState

    val deviceConnectionState: Flow<DeviceConnectionState> = BluetoothBroadcastReceiver.deviceConnectionState
    val bluetoothStatus: Flow<BluetoothState> = BluetoothBroadcastReceiver.bluetoothStatus

    fun checkInitialBluetoothState() = BluetoothBroadcastReceiver.checkInitialBluetoothState()

    fun updateColor() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                _plantState.value = PlantState.values().random()
                delay(5000)
            }
        }

    }

}