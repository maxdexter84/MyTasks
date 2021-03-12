package ru.maxdexter.mytasks.ui.fragments.newtask

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.common.IRepository
import ru.maxdexter.mytasks.ui.entity.UIFile
import ru.maxdexter.mytasks.ui.entity.UITask
import ru.maxdexter.mytasks.utils.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
class NewTaskViewModel(
    uuid: String,
    private val uiTask: UITask,
    private val repository: IRepository, application: Application): AndroidViewModel(application) {
    private val currentUITask = repository.getTaskFromID(uuid).value ?: uiTask.copy()
    private val calendar = Calendar.getInstance(Locale.getDefault())
    private var list = mutableListOf<UIFile>()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val user = repository.getCurrentUser()
    var isOnline: Boolean = true

    enum class NewTaskEvent{
        SAVE,DELETE,ADD_PHOTO,ADD_FILE,IDL,CLOSE
    }
    private val _event = MutableLiveData(NewTaskEvent.IDL)
    val event: LiveData<NewTaskEvent>
        get() = _event


    private val _date = MutableLiveData<String>()
            val date: LiveData<String>
            get() = _date

    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    private val _fileList = MutableLiveData<List<UIFile>>(emptyList())
            val fileList: LiveData<List<UIFile>>
            get() = _fileList

    private val _setAlarm = MutableLiveData<UITask?>(null)
            val setAlarm: LiveData<UITask?>
            get() = _setAlarm

    init {
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
        val task = currentUITask.copy(eventDay = day,eventMonth = month,eventYear = year)
        saveTask(task)
        _date.value = "$d.$mon.$year"

    }

    fun setTime(hour: Int, minute: Int){
        val h = if(hour < 10) "0$hour" else "$hour"
        val m = if (minute < 10) "0$minute" else "$minute"
        val task = currentUITask.copy(eventHour = hour, eventMinute = minute)
        saveTask(task)
        _time.value = "$h : $m"
    }


    @KoinApiExtension
    fun saveTaskChange(title: String, description: String, alarm:Boolean, repeat: Boolean, repeatRange:Int){
        val task = currentUITask.copy(title = title,
            description = description,
            pushMessage = alarm,
            repeat = repeat,
            repeatRangeValue = repeatRange,
            userNumber = user?.phone ?: "")
        saveTask(task)
        setAlarm(task)
    }


    private fun saveFile(uiFile: UIFile){
        list.add(uiFile)
        saveTask(currentUITask.copy(fileList = list))
        _fileList.value = list
    }


     fun createUIFile(pair:Pair<Uri,String>){
        val type = pair.second
        val uri = pair.first.toString()
        val name = handleParseFileName(pair.first.toString())
        val id = currentUITask.id
        saveFile(UIFile(uri = uri,fileType = type,name = name,taskUUID = id))
    }

    fun createFileImage():Uri{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileDir:File = context.filesDir
        val file = File.createTempFile("JPEG_${timeStamp}_",".png",fileDir)
        val uri = file.let { FileProvider.getUriForFile(context, "ru.maxdexter.mytasks.fileprovider", it)  }
        return uri
    }


    private fun saveTask(uiTask: UITask){
        viewModelScope.launch {
            repository.saveTask(uiTask)
        }
    }

    private fun setAlarm(uiTask: UITask){
        if(uiTask.pushMessage) _setAlarm.value = uiTask
    }


    fun deleteTask(){
        viewModelScope.launch {
            repository.deleteTask(currentUITask)
        }
    }


    override fun onCleared() {
        super.onCleared()
        _event.value = NewTaskEvent.IDL
    }
}