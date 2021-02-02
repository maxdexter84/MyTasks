package ru.maxdexter.mytasks.ui.newtask

import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.App
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskFile
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.repository.LoadingResponse
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository
import ru.maxdexter.mytasks.utils.Alarm
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import java.io.IOException
import java.util.*

class NewTaskViewModel (private val repository: LocalDatabase, private val alarmManager: AlarmManager, val context: Context): ViewModel() {
    private val calendar = Calendar.getInstance(Locale.getDefault())
    val user = User()
    private val task = Task()
    private val list = mutableListOf<TaskFile>()
    private val taskWithTaskFile = TaskWithTaskFile()
    val app = App()




   private val selectDate = Calendar.Builder()

    private val _liveDate = MutableLiveData<String>()
            val liveDate: LiveData<String>
            get() = _liveDate

    private val _liveTime = MutableLiveData<String>()
    val liveTime: LiveData<String>
        get() = _liveTime

    private val _fileList = MutableLiveData<List<TaskFile>>()
            val fileList: LiveData<List<TaskFile>>
            get() = _fileList


    init {
        getCurrentDate()
        getCurrentTime()
    }

    private fun getCurrentTime(){
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
       setTime(hour,minute)
    }

    private fun getCurrentDate(){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        setDate(year,month,day)

    }

    fun setDate(year: Int, month: Int, day: Int){
        val mon =  if (month < 10) "0${month + 1}" else month + 1
        val d = if (day  < 10) "0$day" else day
        selectDate.setDate(year,month,day)
        task.eventYear = year
        task.eventMonth = month
        task.eventDay = day
        _liveDate.value = "$d.$mon.$year"

    }

    fun setTime(hour: Int, minute: Int){


        val h = if(hour.toString().length < 2) "0$hour" else hour
        selectDate.setTimeOfDay(hour,minute,0).build()
        task.eventHour = hour
        task.eventMinute = minute
        _liveTime.value = "$h : $minute"
    }

    fun saveTask(title: String, description: String){
        selectDate.build()
        task.description = description
        task.title = title
        taskWithTaskFile.task = task
        saveTaskToDb()
    }

    fun saveFile(requestCode: Int, resultCode: Int, data: Intent?){
        val taskFile = TaskFile()
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data?.data != null){
                taskFile.uri = data.data.toString()
                val tokenArr = data.data.toString().split("/")
                taskFile.name = tokenArr[tokenArr.lastIndex]
            }
        }

        list.add(taskFile)
        _fileList.value = list
        taskWithTaskFile.list = list

    }

    private fun saveTaskToDb(){
        taskWithTaskFile.task = task
        viewModelScope.launch {
            try {
                repository.saveTask(taskWithTaskFile)
                createReminderAlarm(task)
            }catch (e: IOException) {
                Log.e("ERROR_SAVE",e.message.toString())
            }

        }
    }
    private fun createReminderAlarm(task: Task) {
        if (!task.isCompleted) {
            Alarm.createAlarm(
                context,
                task,
                alarmManager
            )
        }
    }



}