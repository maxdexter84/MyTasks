package ru.maxdexter.mytasks.ui.newtask

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class NewTaskViewModel(
                         uuid: String,
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

    enum class NewTaskEvent{
        SAVE,DELETE,ADD_PHOTO,ADD_FILE,IDL,CLOSE
    }
    private val _event = MutableLiveData<NewTaskEvent>(NewTaskEvent.IDL)
    val event: LiveData<NewTaskEvent>
        get() = _event


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

    fun changeEvent(event: NewTaskEvent){
        _event.value = event
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
        val mon =  if (month < 10) "0${month + 1}" else "${month + 1}"
        val d = if (day  < 10) "0$day" else "$day"
        task.eventYear = year
        task.eventMonth = month
        task.eventDay = day
        _liveDate.value = "$d.$mon.$year"

    }

    fun setTime(hour: Int, minute: Int){
        val h = if(hour < 10) "0$hour" else "$hour"
        val m = if (minute < 10) "0$minute" else "$minute"
        task.eventHour = hour
        task.eventMinute = minute
        _liveTime.value = "$h : $m"
    }


    @KoinApiExtension
    fun saveTaskChange(title: String, description: String, alarm:Boolean, repeat: Boolean, repeatRange:Int){
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

    private fun saveFile(taskFile: TaskFile){
        list.add(taskFile)
        _fileList.value = list
    }


     fun createTaskFile(pair:Pair<Uri,String>){
        val type = pair.second
        val uri = pair.first.toString()
        val name = handleParseFileName(pair.first.toString())
        val id = task.id
        saveFile(TaskFile(uri = uri,fileType = type,name = name,taskUUID = id))
    }

    fun createFileImage():Uri{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileDir:File = context.filesDir
        val file = File.createTempFile("JPEG_${timeStamp}_",".png",fileDir)
        val uri = file.let { FileProvider.getUriForFile(context, "ru.maxdexter.mytasks.fileprovider", it)  }
        return uri
    }





    @KoinApiExtension
    private fun saveTaskToDb(){

            try {
                if (isOnline){
                    viewModelScope.launch {
                        taskWithTaskFile.task.saveToCloud = true
                        repository.saveTask(taskWithTaskFile)
                    }
                    if(remoteRepository.getCurrentUser() != null){
                        saveTaskToFireStore(taskWithTaskFile)
                        saveFileToStorage(taskWithTaskFile)
                    }


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

    override fun onCleared() {
        super.onCleared()
        _event.value = NewTaskEvent.IDL
    }



}