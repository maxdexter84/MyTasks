package ru.maxdexter.mytasks.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.models.Hour
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository
import java.time.Year

class CalendarViewModel(private val repository: LocalDatabase) : ViewModel() {

    private var _listTaskWithTaskFile = MutableLiveData<List<TaskWithTaskFile>>()
        val listTaskFile: LiveData<List<TaskWithTaskFile>>
        get() = _listTaskWithTaskFile

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
            var hour = Hour(i, ("$i" + "59").toInt())
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


}