package ru.maxdexter.mytasks.ui.newtask

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskFile
import ru.maxdexter.mytasks.models.User
import java.util.*

class NewTaskViewModel : ViewModel() {
    private val calendar = Calendar.getInstance(Locale.getDefault())
    val user = User()
    val task = Task()
    val list: RealmList<TaskFile> = RealmList()

    private val REQUEST_CODE: Int = 123

   private val selectDate = Calendar.Builder()

    private val _liveDate = MutableLiveData<String>()
            val liveDate: LiveData<String>
            get() = _liveDate

    private val _liveTime = MutableLiveData<String>()
    val liveTime: LiveData<String>
        get() = _liveTime


    init {
        getCurrentDate()
        getCurrentTime()
    }

    private fun getCurrentTime(){
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
       setTime(hour,minute)
    }

    private fun getCurrentDate(){
        val year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        setDate(year,month,day)

    }

    fun setDate(year: Int, month: Int, day: Int){
        val mon =  if (month.toString().length < 2) "0${month + 1}" else month + 1
        val d = if (day.toString().length < 2) "0$day" else day
        selectDate.setDate(year,month,day)
        _liveDate.value = "$d.$mon.$year"

    }

    fun setTime(hour: Int, minute: Int){
        val h = if(hour.toString().length < 2) "0$hour" else hour
        selectDate.setTimeOfDay(hour,minute,0)
        _liveTime.value = "$h : $minute"
    }

    fun saveTask(title: String, description: String){
        task.description = description
        task.title = title
        selectDate.build()
    }

    fun saveFile(requestCode: Int, resultCode: Int, data: Intent?){
        var fileUri: Uri? = null
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if (data?.data != null){
                fileUri = data.data
            }
        }
        val taskFile = TaskFile()
        taskFile.uri = fileUri.toString()
        list.add(taskFile)
        task.file = list
    }

}