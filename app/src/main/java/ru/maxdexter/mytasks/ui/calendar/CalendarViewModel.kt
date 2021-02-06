package ru.maxdexter.mytasks.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.domen.models.Hour
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import java.util.*

class CalendarViewModel(private val repository: LocalDatabase) : ViewModel() {

    private var _listTaskWithTaskFile = MutableLiveData<List<TaskWithTaskFile>>(emptyList())
        val listTaskFile: LiveData<List<TaskWithTaskFile>>
        get() = _listTaskWithTaskFile

    val calendar = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault())
    private var _selectedTask = MutableLiveData("")
            val selectedTask: LiveData<String>
            get() = _selectedTask

    init {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        loadData(year, month, day)
    }

     fun loadData(year: Int, month: Int, day: Int){
         viewModelScope.launch {
             repository.getAllTaskWithTaskFile(year,month,day).collect {
                 _listTaskWithTaskFile.value = it
             }

         }
    }


    fun updateData(
        it: List<TaskWithTaskFile>
    ): MutableList<Hour> {
        val hourList = mutableListOf<Hour>()
        for (i in 0..23 step 1) {
            val taskWithTaskFile = mutableListOf<TaskWithTaskFile>()
            for (item in it) {
                if (item.task?.eventHour == i) {
                    taskWithTaskFile.add(item)

                }
            }
            hourList.add(Hour(i, ("$i" + "59").toInt(), taskWithTaskFile))
        }
        return hourList
    }

    fun selectedTask(uuid: String){
        _selectedTask.value = uuid

    }

}