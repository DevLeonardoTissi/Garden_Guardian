package br.com.leonardo.gardenguardian.notification

import android.app.NotificationManager
import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Notification(private val context:Context): KoinComponent {

    val notificationManager : NotificationManager by inject()

}