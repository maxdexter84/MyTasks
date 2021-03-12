package ru.maxdexter.mytasks.utils.alarm

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


import ru.maxdexter.mytasks.data.localdatabase.LocalDatabaseImpl

@KoinApiExtension
class BootReceiver : BroadcastReceiver() , KoinComponent{
    private val repository: LocalDatabaseImpl = get()
    companion object {
        private const val TAG = "BootReceiver"
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                Log.d(TAG, "onReceive: intent action is correct")
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                CoroutineScope(Dispatchers.Main).launch {
                   repository.getAllTask().collect { list ->
                      if (!list.isNullOrEmpty()) {
                          list.forEach { taskWithFile ->
                              if (!taskWithFile.task.isCompleted) {
                                  Alarm.createAlarm(
                                      context.applicationContext,
                                      taskWithFile.task,
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
}