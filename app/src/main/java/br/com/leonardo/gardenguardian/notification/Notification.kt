package br.com.leonardo.gardenguardian.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.ui.CHANNEL_IDENTIFIER
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Notification(private val context:Context): KoinComponent {

    private val notificationManager : NotificationManager by inject()

    companion object {
        var id = 1
            private set
    }


    fun show(
        title: String,
        description: String,
        img: String? = null,
        iconId: Int,
        isOnGoing: Boolean? = false,
        isAutoCancel: Boolean? = true,
        progress: Int? = null,
        exclusiveId: Int? = null,
        actionIcon: Int? = null,
        actionTitle: String? = null,
        actionIntent: PendingIntent? = null,
        secondActionIcon: Int? = null,
        secondActionTitle: String? = null,
        secondActionIntent: PendingIntent? = null,
        contentIntent: PendingIntent? = null
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val image = trySearchImg(img)
            val style = createStyle(image, description)
            val notification =
                createNotification(
                    title,
                    description,
                    style,
                    iconId,
                    isOnGoing ?: false,
                    isAutoCancel ?: true,
                    progress,
                    actionIcon,
                    actionTitle,
                    actionIntent,
                    secondActionIcon,
                    secondActionTitle,
                    secondActionIntent,
                    contentIntent
                )

            notificationManager.notify(exclusiveId ?: id, notification)
            exclusiveId ?: id++
        }
    }

    private suspend fun trySearchImg(img: String?): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(img)
            .build()
        return context.imageLoader.execute(request).drawable?.toBitmap()
    }

    private fun createNotification(
        title: String,
        description: String,
        style: NotificationCompat.Style,
        iconId: Int,
        isOnGoing: Boolean = false,
        isAutoCancel: Boolean = true,
        progress: Int? = null,
        actionIcon: Int? = null,
        actionTitle: String? = null,
        actionIntent: PendingIntent? = null,
        secondActionIcon: Int? = null,
        secondActionTitle: String? = null,
        secondActionIntent: PendingIntent? = null,
        contentIntent: PendingIntent? = null


    ): Notification {
        val builder = NotificationCompat.Builder(context, CHANNEL_IDENTIFIER)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(iconId)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(isAutoCancel)
            .setStyle(style)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setColor(ContextCompat.getColor(context, R.color.primary))
            .setOngoing(isOnGoing)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentIntent)

        progress?.let { progressNonNull ->
            builder.setProgress(100, progressNonNull, false)
        }

        addActionIfNotNull(builder, actionIcon, actionTitle, actionIntent)
        addActionIfNotNull(builder, secondActionIcon, secondActionTitle, secondActionIntent)

        return builder.build()
    }

    private fun createStyle(img: Bitmap?, description: String): NotificationCompat.Style {
        return img?.let {
            NotificationCompat.BigPictureStyle().bigPicture(it)
        } ?: NotificationCompat.BigTextStyle().bigText(description)
    }

    private fun addActionIfNotNull(builder: NotificationCompat.Builder, icon: Int?, title: String?, intent: PendingIntent?) {
        if (listOf(icon, title, intent).all { it != null }) {
            builder.addAction(icon!!, title, intent)
        }
    }




}