package br.com.leonardo.gardenguardian.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.model.Plant
import br.com.leonardo.gardenguardian.repository.PlantRepository
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.utils.enums.BluetoothState
import br.com.leonardo.gardenguardian.utils.enums.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: PlantRepository) : ViewModel() {

    val plantState: Flow<PlantState?> = BluetoothPlantMonitorService.plantState

    val deviceConnectionState: Flow<DeviceConnectionState> = BluetoothBroadcastReceiver.deviceConnectionState

    val bluetoothStatus: Flow<BluetoothState> = BluetoothBroadcastReceiver.bluetoothStatus

    val plant: Flow<Plant> = repository.search()

    fun checkInitialBluetoothState() = BluetoothBroadcastReceiver.checkInitialBluetoothState()

    fun updateImg(url:String?){
        viewModelScope.launch {
            repository.insertUrl(url)
        }
    }

}