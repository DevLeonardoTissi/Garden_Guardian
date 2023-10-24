package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.model.Plant
import br.com.leonardo.gardenguardian.repository.PlantRepository
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.utils.BluetoothState
import br.com.leonardo.gardenguardian.utils.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.PlantState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: PlantRepository) : ViewModel() {


    val plantState: Flow<PlantState?> = BluetoothPlantMonitorService.plantState


    private val _deviceConnectionState: MutableStateFlow<DeviceConnectionState> =
        MutableStateFlow(DeviceConnectionState.DISCONNECTED)


    val deviceConnectionState: Flow<DeviceConnectionState> = _deviceConnectionState

    val bluetoothStatus: Flow<BluetoothState> = BluetoothBroadcastReceiver.bluetoothStatus

    val plant: Flow<Plant> = repository.search()

    fun checkInitialBluetoothState() = BluetoothBroadcastReceiver.checkInitialBluetoothState()

    fun deviceConnected() {
        _deviceConnectionState.value = DeviceConnectionState.CONNECTED

//        viewModelScope.launch {
//            repository.search().collect{ plants ->
//                Log.i("TAG", "deviceConnected: $plants")
//            }
//        }


    }

    fun deviceDisconnected() {
        _deviceConnectionState.value = DeviceConnectionState.DISCONNECTED
    }


}