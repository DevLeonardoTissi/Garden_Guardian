package br.com.leonardo.gardenguardian.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.notification.Notification
import br.com.leonardo.gardenguardian.repository.SettingsRepository
import br.com.leonardo.gardenguardian.ui.activity.MainActivity
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import br.com.leonardo.gardenguardian.widget.MonitorWidget
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BluetoothPlantMonitorService : Service() {

    private val notificationManager: NotificationManager by inject()

    private val settingsRepository: SettingsRepository by inject()

    private var bluetoothCoroutineJob: Job? = null

    private val settings = settingsRepository.search()


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startReadingData()
        return START_NOT_STICKY
    }

    companion object {
        private val _plantState: MutableStateFlow<PlantState?> = MutableStateFlow(null)
        val plantState: Flow<PlantState?> = _plantState

        private val _moisturePercentageChart: MutableStateFlow<List<ChartEntry>> =
            MutableStateFlow(listOf(entryOf(0f, 0f)))
        val moisturePercentageChart: Flow<List<ChartEntry>> = _moisturePercentageChart

        private val _moisturePercent: MutableStateFlow<Int?> = MutableStateFlow(null)
        val moisturePercent: Flow<Int?> = _moisturePercent


    }

    private fun startReadingData() {

        bluetoothCoroutineJob = CoroutineScope(Dispatchers.IO).launch {

            while (true) {
                try {

                    BluetoothSocketSingleton.socket?.let {
                        val inputStream = it.inputStream
                        val buffer = ByteArray(1024)
                        val bytes = inputStream.read(buffer)
                        val humidityPercentage = String(buffer, 0, bytes).trim().toIntOrNull()


                        humidityPercentage?.let { percentage ->

                            _moisturePercent.value = percentage

                            if (_moisturePercentageChart.value.size >= 10) _moisturePercentageChart.value =
                                emptyList()

                            _moisturePercentageChart.value += entryOf(
                                _moisturePercentageChart.value.size.toFloat(),
                                percentage
                            )

                            when {
                                (humidityPercentage < 40f) -> {
                                    _plantState.value = PlantState.LowWater
                                }

                                (humidityPercentage in 40..60) -> {
                                    _plantState.value = PlantState.Alert
                                }

                                else -> {
                                    _plantState.value = PlantState.Ok
                                }
                            }


                            if (settings.firstOrNull()?.showNotification == true) {
                                showNotification(percentage)
                            } else {
                                notificationManager.cancel(Int.MAX_VALUE)
                            }
                        }
                    }

                } catch (_: Exception) {
                    stopSelf()
                } finally {
                    MonitorWidget().updateWidget(applicationContext)
                }
            }
        }
    }


    private fun showNotification(humidityPercent: Int) {


        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, Intent(applicationContext, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        Notification(applicationContext).show(
            title = applicationContext.getString(
                R.string.VerificationNotificationTitle,
                humidityPercent.toString()
            ),
            description = applicationContext.getString(R.string.VerificationNotificationDescription),
            iconId = R.drawable.ic_grass,
            isOnGoing = true,
            isAutoCancel = false,
            exclusiveId = Int.MAX_VALUE,
            progress = humidityPercent,
            contentIntent = pendingIntent
        )

    }

    private fun stopRead() {
        bluetoothCoroutineJob?.cancel()
        notificationManager.cancel(Int.MAX_VALUE)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }

    override fun onDestroy() {
        stopRead()
    }


}