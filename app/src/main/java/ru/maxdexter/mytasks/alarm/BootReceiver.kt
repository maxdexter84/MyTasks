package ru.maxdexter.mytasks.alarm

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.repository.Repository
import ru.maxdexter.mytasks.repository.localdatabase.RoomDb
import ru.maxdexter.mytasks.utils.Alarm

class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                Log.d(TAG, "onReceive: intent action is correct")
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val db = RoomDb.invoke(context)
                val repository = Repository(db.getDao())
                CoroutineScope(Dispatchers.Main).launch {
                  val  tasks = repository.getAllTask().value

                    if (!tasks.isNullOrEmpty()) {
                        tasks.forEach { it ->
                            if (!it.isCompleted) {
                                Alarm.createAlarm(
                                    context.applicationContext,
                                    it,
                                    alarmManager
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}