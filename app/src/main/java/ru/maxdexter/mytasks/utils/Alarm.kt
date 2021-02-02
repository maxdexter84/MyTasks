package ru.maxdexter.mytasks.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import ru.maxdexter.mytasks.alarm.BootReceiver
import ru.maxdexter.mytasks.alarm.NotificationReceiver
import ru.maxdexter.mytasks.models.Task
import java.util.*

class Alarm {

    companion object {
        private const val TAG = "AlarmUtil"

        @SuppressLint("SimpleDateFormat")
        private fun getTriggerTime(day: Int, month: Int, year: Int, hour: Int, minute: Int): Long {
            val eventDate = mapDateToLong(year, month, day, hour, minute)
            val timeDiff = eventDate - Calendar.getInstance().timeInMillis
            return SystemClock.elapsedRealtime() + timeDiff
        }

        private fun getRepeatTime(repeatRange: String): Long {
            return when (repeatRange) {
                TimeRange.MINUTE.name-> TimeRange.MINUTE.value
                TimeRange.HOUR.name -> TimeRange.HOUR.value
                TimeRange.DAY.name -> TimeRange.DAY.value
                TimeRange.MONTH.name -> TimeRange.MONTH.value
                else -> TimeRange.YEAR.value
            }
        }

        fun createAlarm(
            context: Context,
            task: Task,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = NotificationReceiver.getAlarmPendingIntent(
                context,
                task
            )

            if (task.repeat) {
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    getTriggerTime(task.eventDay,task.eventMonth,task.eventYear,task.eventHour,task.eventMinute),
                    getRepeatTime(task.repeatRange),
                    alarmPendingIntent
                )
            } else {
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    getTriggerTime(task.eventDay,task.eventMonth,task.eventYear,task.eventHour,task.eventMinute),
                    alarmPendingIntent
                )
            }
            updateAlarmWhenReboot(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        }

        fun cancelAlarm(
            context: Context,
            task: Task,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = NotificationReceiver.getAlarmPendingIntent(
                context,
                task
            )
            Log.d(TAG, "cancelAlarm: ")
            alarmManager.cancel(alarmPendingIntent)
            updateAlarmWhenReboot(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        }


        private fun updateAlarmWhenReboot(context: Context, state:Int){
            val receiver = ComponentName(context, BootReceiver::class.java)
            context.packageManager.setComponentEnabledSetting(
                receiver,
                state,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}