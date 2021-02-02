package ru.maxdexter.mytasks

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.utils.Alarm
import ru.maxdexter.mytasks.utils.CHANNEL_DESCRIPTION
import ru.maxdexter.mytasks.utils.CHANNEL_ID
import ru.maxdexter.mytasks.utils.CHANNEL_NAME

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)
    }


    private fun createNotificationChannel(channelId: String, channelName: String, description:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }
            notificationChannel.description = description
            val notificationManager = ContextCompat.getSystemService(this.applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}