package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.*
import java.io.*
import java.util.*
import kotlin.properties.Delegates

class NewTaskViewModel(
                        private val uuid: String,
                        private val repository: LocalDatabase,
                        private val remoteRepository: RemoteDataProvider,
                        private val storage: DataStorage,
                        application: Application): AndroidViewModel(application) {

    private val calendar = Calendar.getInstance(Locale.getDefault())

    private var task = Task()
    private var list = mutableListOf<TaskFile>()
    private var taskWithTaskFile = TaskWithTaskFile()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val user = remoteRepository.getCurrentUser()
    var isOnline: Boolean = true



    private val _liveDate = MutableLiveData<String>()
            val liveDate: LiveData<String>
            get() = _liveDate

    private val _liveTime = MutableLiveData<String>()
    val liveTime: LiveData<String>
        get() = _liveTime

    private val _fileList = MutableLiveData<List<TaskFile>>(emptyList())
            val fileList: LiveData<List<TaskFile>>
            get() = _fileList

    private val _setAlarm = MutableLiveData<Task?>(null)
            val setAlarm: LiveData<Task?>
            get() = _setAlarm

    private val _titleAndDescription = MutableLiveData<Pair<String, String>?>(null)
            val titleAndDescription: LiveData<Pair<String, String>?>
            get() = _titleAndDescription
    init {
        if (uuid != "empty"){
            uuid.let { getTask(it) }
        }
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
        task.eventYear = year
        task.eventMonth = month
        task.eventDay = day
        _liveDate.value = "$d.$mon.$year"

    }

    fun setTime(hour: Int, minute: Int){
        val h = if(hour < 10) "0$hour" else hour
        val m = if (minute < 10) "0$minute" else minute
        task.eventHour = hour
        task.eventMinute = minute
        _liveTime.value = "$h : $m"
    }

    fun saveTaskChange(title: String, description: String,alarm:Boolean,repeat: Boolean, repeatRange:Int){
        task.description = description
        task.title = title
        task.pushMessage = alarm
        task.repeat = repeat
        task.repeatRangeValue = if(repeat)TimeRange.values()[repeatRange].value else 0L
        task.userNumber = user?.phone ?: " "
        taskWithTaskFile.task = task
        taskWithTaskFile.list = list
        saveTaskToDb()
    }

    fun saveFile(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data != null && data.data != null){
                list.add(createTaskFile(data))
            }
        }
        _fileList.value = list

    }

    private fun createTaskFile(data:Intent): TaskFile{
        val type = data.resolveType(context).toString()
        val uri = data.data.toString()
        val name = handleParseFileName(uri)
        val id = task.id
        return TaskFile(uri = uri,fileType = type,name = name,taskUUID = id)
    }


    @KoinApiExtension
    private fun saveTaskToDb(){

            try {
                if (isOnline){
                    viewModelScope.launch {
                        taskWithTaskFile.task.saveToCloud = true
                        repository.saveTask(taskWithTaskFile)
                    }
                    saveTaskToFireStore(taskWithTaskFile)
                    saveFileToStorage(taskWithTaskFile)

                }else {
                    viewModelScope.launch {
                        taskWithTaskFile.task.saveToCloud = false
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

    private  fun saveTaskToFireStore(taskWithTaskFile: TaskWithTaskFile) {
        GlobalScope.launch {
            remoteRepository.saveTask(taskWithTaskFileToTaskFS(taskWithTaskFile))
        }
    }


    private fun saveFileToStorage(taskWithTaskFile: TaskWithTaskFile){
        if (taskWithTaskFile.list.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                storage.saveFileToStorage(taskWithTaskFile)
            }
        }
    }


    fun deleteTask(){
        viewModelScope.launch {
            repository.deleteTask(task)
            taskWithTaskFile.list.let { list1 ->
                list1.forEach { file->
                file.let { repository.deleteTaskFile(it) }
            } }

        }
    }






    private fun getTask(uuid: String){
        viewModelScope.launch {
            repository.getCurrentTask(uuid).collect {it->
                if (it != null ){
                    taskWithTaskFile = it
                    task = taskWithTaskFile.task
                    list.addAll(taskWithTaskFile.list)
                    _fileList.value = list
                    setTime(task.eventHour, task.eventMinute)
                    setDate(task.eventYear,task.eventMonth,task.eventDay)
                    _titleAndDescription.value = task.title to task.description
                }

            }
        }
    }







}