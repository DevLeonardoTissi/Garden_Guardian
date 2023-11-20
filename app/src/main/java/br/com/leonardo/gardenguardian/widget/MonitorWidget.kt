package br.com.leonardo.gardenguardian.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.broadcastReceiver.BluetoothBroadcastReceiver
import br.com.leonardo.gardenguardian.services.BluetoothPlantMonitorService
import br.com.leonardo.gardenguardian.ui.theme.md_theme_light_background
import br.com.leonardo.gardenguardian.utils.enums.DeviceConnectionState
import br.com.leonardo.gardenguardian.utils.enums.PlantState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val widgetActionKey = ActionParameters.Key<String>("widgetActionKey")

class MonitorWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent { MonitorWidgetContent() }
    }


    @Composable
    fun MonitorWidgetContent() {

        val context = LocalContext.current

        val moisturePercent = BluetoothPlantMonitorService.moisturePercent.collectAsState(
            initial = null
        )
        val deviceState =
            BluetoothBroadcastReceiver.deviceConnectionState.collectAsState(initial = DeviceConnectionState.DISCONNECTED)

        val plantState = BluetoothPlantMonitorService.plantState.collectAsState(initial = null)


        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(md_theme_light_background.copy(alpha = 0.8f)).padding(16.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {


            val imageID = when (deviceState.value) {
                DeviceConnectionState.DISCONNECTED -> R.drawable.ic_grass
                DeviceConnectionState.CONNECTED -> {
                    when (plantState.value) {
                        PlantState.Ok -> R.drawable.ic_grass_plant_state_ok
                        PlantState.LowWater -> R.drawable.ic_grass_plant_state_low_water
                        PlantState.Alert -> R.drawable.ic_grass_plant_state_alert
                        else -> {
                            R.drawable.ic_grass
                        }
                    }
                }

            }

            Image(
                contentDescription = context.getString(R.string.monitorWidgetImageDescription),
                provider = ImageProvider(imageID),
                modifier = GlanceModifier.size(50.dp)
            )

            val textPresentation = if (deviceState.value == DeviceConnectionState.CONNECTED) {
                context.getString(R.string.monitorWidgetTextConnected, moisturePercent.value)
            } else {
                context.getString(R.string.monitorWidgetTextDisconnected)
            }

            Text(
                text = textPresentation,
                modifier = GlanceModifier.padding(12.dp)
            )


            Button(text = context.getString(R.string.monitorWidgetButtonRefreshText),
                onClick = {
                    actionRunCallback<RefreshWidgetActionCallback>(
                        actionParametersOf(widgetActionKey to "REFRESH")
                    )
                }
            )

        }
    }

    fun updateWidget(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val manager = GlanceAppWidgetManager(context)
            val widget = MonitorWidget()
            val glanceIds = manager.getGlanceIds(widget.javaClass)
            glanceIds.forEach { glanceId ->
                widget.update(context, glanceId)
            }
        }
    }


    class RefreshWidgetActionCallback : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
            val action: String = parameters[widgetActionKey] ?: return
            if (action != "REFRESH") {
                return
            } else {
                MonitorWidget().update(context, glanceId)
            }
        }

    }

}



