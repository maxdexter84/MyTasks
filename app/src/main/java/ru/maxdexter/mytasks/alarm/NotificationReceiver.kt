package ru.maxdexter.mytasks.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.maxdexter.mytasks.MainActivity
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.utils.*

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val reminderIdentifier = intent?.extras?.getString(INTENT_TASK_UUID)
        val title = intent?.extras?.getString(INTENT_TITLE)
        val taskTimeText = intent?.extras?.getString(INTENT_TIME)
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(INTENT_TASK_UUID,reminderIdentifier)
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE,notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(taskTimeText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }

        val notificationManager = context?.let { ContextCompat.getSystemService(it,NotificationManager::class.java) } as NotificationManager
        notificationManager.notify(REQUEST_CODE,notificationBuilder?.build())

    }

    companion object {
        fun getAlarmPendingIntent(
            context: Context,
            task:Task
        ): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.apply {
                putExtra(INTENT_TASK_UUID, task.id)
                putExtra(INTENT_TITLE, task.title)
                putExtra(INTENT_TIME, "${task.eventHour}:${task.eventMinute}")
            }

            return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }


    }
}