package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.models.User
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.domen.repository.firebase.Auth
import ru.maxdexter.mytasks.domen.repository.firebase.FireStoreProvider
import ru.maxdexter.mytasks.utils.Alarm
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import ru.maxdexter.mytasks.utils.handleParseFileName
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.*
import java.util.*

class NewTaskViewModel (private val repository: LocalDatabase, private val remoteRepository: RemoteDataProvider ,application: Application): AndroidViewModel(application) {
    private val calendar = Calendar.getInstance(Locale.getDefault())
    val user = User()
    private val task = Task()
    private val list = mutableListOf<TaskFile>()
    private val taskWithTaskFile = TaskWithTaskFile()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext




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

    private val _setAlarm = MutableLiveData<Task?>(null)
            val setAlarm: LiveData<Task?>
            get() = _setAlarm

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
        val h = if(hour < 10) "0$hour" else hour
        val m = if (minute < 10) "0$minute" else minute
        selectDate.setTimeOfDay(hour,minute,0).build()
        task.eventHour = hour
        task.eventMinute = minute
        _liveTime.value = "$h : $m"
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
            if (data != null && data.data != null){
               val type = data.resolveType(context).toString()
               val dir = context.getExternalFilesDir(null)
                val file = File(dir,data.dataString ?: "")
                val uri = data.dataString?.toUri()
                if (uri != null){
                    taskFile.name = handleParseFileName(uri.toString())
                }
                taskFile.fileType = type
                taskFile.uri = data.dataString.toString()
                viewModelScope.launch {
                    try {
                        // Получаем InputStream, из которого будем декодировать Bitmap


                    }catch (e: IOException){
                        Log.e("IOException",e.message.toString())
                    }catch (e: FileNotFoundException){
                        Log.e("FileNotFoundException",e.message.toString())
                    }

                }

            }
        }

        list.add(taskFile)
        _fileList.value = list
        taskWithTaskFile.list = list

    }

    @KoinApiExtension
    private fun saveTaskToDb(){
        taskWithTaskFile.task = task
        viewModelScope.launch {
            try {
                repository.saveTask(taskWithTaskFile)
                _setAlarm.value = task
                if(remoteRepository.getCurrentUser() !=  null){
                    remoteRepository.saveTask(taskWithTaskFileToTaskFS(taskWithTaskFile))
                }else{Log.e("SAVE_TASK_TO_FIRESTORE", "the user is not logged in") }
                }catch (e: IOException) {
                Log.e("ERROR_SAVE",e.message.toString())
            }

        }
    }




}