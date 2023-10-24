package br.com.leonardo.gardenguardian.services
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.IBinder
import android.util.Log
import br.com.leonardo.gardenguardian.utils.BluetoothSocketSingleton
import br.com.leonardo.gardenguardian.utils.PlantState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.koin.android.ext.android.inject
import java.io.IOException

class BluetoothPlantMonitorService: Service() {

    private val notificationManager: NotificationManager by inject()
    private var isReadingData = true
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startReadingData()
        Log.i("TAG", "onStartCommand: Iniciou comando")

        return START_NOT_STICKY
    }

    companion object{
        private val _plantState: MutableStateFlow<PlantState?> = MutableStateFlow(null)
        val plantState: Flow<PlantState?> = _plantState

    }


    private fun startReadingData()  {

        CoroutineScope(Dispatchers.IO).launch {
            while (isReadingData) {
                try {
                    BluetoothSocketSingleton.socket?.let {
                        val inputStream = it.inputStream
                        val buffer = ByteArray(1024)
                        val bytes = inputStream.read(buffer)
                        val message = String(buffer, 0, bytes)
                        Log.i("dispositivos", message)

                        when (message){
                            "precisa regar" -> _plantState.value = PlantState.LowWater
                            "quase ok" -> _plantState.value = PlantState.Alert
                            "ok" -> _plantState.value = PlantState.Ok
                        }
                    }


                } catch (e: IOException) {
                    // Lide com a exceção adequadamente
                    isReadingData = false
                }
            }


        }
    }

    private fun stopRear(){
        isReadingData = false
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
    }

    override fun onDestroy() {
        stopRear()
    }


}