package ru.maxdexter.mytasks.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.maxdexter.mytasks.MainActivity
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.utils.CHANNEL_ID
import ru.maxdexter.mytasks.utils.INTENT_TASK_UUID
import ru.maxdexter.mytasks.utils.INTENT_TITLE
import ru.maxdexter.mytasks.utils.REQUEST_CODE

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val reminderIdentifier = intent?.extras?.getString(INTENT_TASK_UUID)
        val title = intent?.extras?.getString(INTENT_TITLE)

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
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
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