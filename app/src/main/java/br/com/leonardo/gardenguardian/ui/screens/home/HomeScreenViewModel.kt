package br.com.leonardo.gardenguardian.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.model.Plant
import br.com.leonardo.gardenguardian.model.Settings
import br.com.leonardo.gardenguardian.repository.PlantRepository
import br.com.leonardo.gardenguardian.repository.SettingsRepository
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.utils.enums.BluetoothState
import br.com.leonardo.gardenguardian.utils.enums.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val plantRepository: PlantRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val plantState: Flow<PlantState?> = BluetoothPlantMonitorService.plantState

    val deviceConnectionState: Flow<DeviceConnectionState> =
        BluetoothBroadcastReceiver.deviceConnectionState

    val bluetoothStatus: Flow<BluetoothState> = BluetoothBroadcastReceiver.bluetoothStatus

    val plant: Flow<Plant> = plantRepository.search()

    val settings: Flow<Settings> = settingsRepository.search()

    val moisturePercentage = BluetoothPlantMonitorService.moisturePercentageChart



    fun checkInitialBluetoothState() = BluetoothBroadcastReceiver.checkInitialBluetoothState()

    fun updateImg(url: String?) {
        viewModelScope.launch {
            plantRepository.insertUrl(url)
        }
    }

    fun updateShowNotificationSetting(showNotification: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateShowNotification(showNotification)
        }
    }

}