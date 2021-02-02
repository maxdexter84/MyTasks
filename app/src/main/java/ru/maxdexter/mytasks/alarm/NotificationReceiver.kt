package ru.maxdexter.mytasks.alarm

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ru.maxdexter.mytasks.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, "MyChannel")
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("text"))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(84,notification)


    }
}