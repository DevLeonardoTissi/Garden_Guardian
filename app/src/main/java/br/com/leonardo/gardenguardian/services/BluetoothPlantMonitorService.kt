package br.com.leonardo.gardenguardian.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.notification.Notification
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.IOException

class BluetoothPlantMonitorService : Service() {

    private val notificationManager: NotificationManager by inject()
    private var isReadingData = true


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
    }

    private fun startReadingData() {

        CoroutineScope(Dispatchers.IO).launch {
            while (isReadingData) {
                try {
                    BluetoothSocketSingleton.socket?.let {
                        val inputStream = it.inputStream
                        val buffer = ByteArray(1024)
                        val bytes = inputStream.read(buffer)
                        val message = String(buffer, 0, bytes).trim().toIntOrNull()


                        message?.let {
                            when {
                                (message < 40) -> {
                                    _plantState.value = PlantState.LowWater
                                }

                                (message in 40..60) -> {
                                    _plantState.value = PlantState.Alert
                                }

                                else -> {
                                    _plantState.value = PlantState.Ok
                                }
                            }

                            showNotification(message)

                        }


                    }


                } catch (_: IOException) {
                    stopSelf()
                }
            }

        }
    }


    private fun showNotification(progress: Int) {

        Notification(applicationContext).show(
            title = "Umidade do solo: $progress%",
            description = "Umidade do solo: $progress%",
            iconId = R.drawable.ic_grass,
            isOnGoing = true,
            isAutoCancel = false,
            exclusiveId = Int.MAX_VALUE,
            progress = progress
        )

    }

    private fun stopRead() {
        isReadingData = false
        notificationManager.cancel(Int.MAX_VALUE)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }

    override fun onDestroy() {
        stopRead()
    }


}