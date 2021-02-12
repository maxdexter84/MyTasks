package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.CheckNetwork
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import ru.maxdexter.mytasks.utils.handleParseFileName
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.*
import java.util.*
import kotlin.properties.Delegates

class NewTaskViewModel (private val repository: LocalDatabase,
                        private val remoteRepository: RemoteDataProvider,
                        private val storage: DataStorage,
                        application: Application): AndroidViewModel(application) {

    private val calendar = Calendar.getInstance(Locale.getDefault())

    private val task = Task()
    private val taskFS = TaskFS()
    private val list = mutableListOf<TaskFile>()
    private val taskWithTaskFile = TaskWithTaskFile()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val user = remoteRepository.getCurrentUser()
    var isOnline: Boolean = true

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

    fun saveTaskChange(title: String, description: String){
        selectDate.build()
        task.description = description
        task.title = title
        taskWithTaskFile.task = task
        saveTaskToDb()
    }

    fun saveFile(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data != null && data.data != null){
                list.add(createTaskFile(data))
            }
        }
        _fileList.value = list
        taskWithTaskFile.list = list
    }

    private fun createTaskFile(data:Intent): TaskFile{
        val type = data.resolveType(context).toString()
        val uri = data.dataString.toString()
        val name = handleParseFileName(uri)
        return TaskFile(uri = uri,fileType = type,name = name)
    }


    private fun saveFileToStorage(taskWithTaskFile: TaskWithTaskFile){
        if (taskWithTaskFile.list.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                storage.saveFileToStorage(taskWithTaskFile)
            }
        }


    }
    @KoinApiExtension
    private fun saveTaskToDb(){
        taskWithTaskFile.task = task
        taskWithTaskFile.task.userNumber = user?.phone ?: " "

            try {
                if (isOnline){
                    viewModelScope.launch {
                        saveTaskToFireStore(taskWithTaskFile)
                        taskWithTaskFile.task.saveToCloud = true
                        repository.saveTask(taskWithTaskFile)
                    }
                    saveFileToStorage(taskWithTaskFile)

                }else {
                    viewModelScope.launch {
                        repository.saveTask(taskWithTaskFile)
                    }
                }

                if (task.pushMessage){
                    _setAlarm.value = task
                }


                }catch (e: IOException) {
                Log.e("ERROR_SAVE",e.message.toString())
            }



    }

    private suspend fun saveTaskToFireStore(taskWithTaskFile: TaskWithTaskFile) {
            remoteRepository.saveTask(taskWithTaskFileToTaskFS(taskWithTaskFile))

    }










}