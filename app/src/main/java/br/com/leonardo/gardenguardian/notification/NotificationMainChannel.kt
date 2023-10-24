package br.com.leonardo.gardenguardian.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.ui.CHANNEL_IDENTIFIER

class NotificationMainChannel(
    private val context: Context,
    private val manager: NotificationManager


) {
    fun createChannel() {
        val name = context.getString(R.string.channel_name)
        val description = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_IDENTIFIER, name, importance)
        channel.description = description
        manager.createNotificationChannel(channel)
    }
}