package ru.maxdexter.mytasks.ui.fragments.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.ui.entity.Hour
import ru.maxdexter.mytasks.domen.common.IRepository
import ru.maxdexter.mytasks.ui.entity.UITask
import java.util.*

class CalendarViewModel(private val repository: IRepository) : ViewModel() {

    private var _listUITask = MutableLiveData<List<UITask>>(emptyList())
        val listUITask: LiveData<List<UITask>>
        get() = _listUITask

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
             repository.getCurrentDateTasks(year,month,day).collect {
                 _listUITask.value = it
             }

         }
    }

    fun updateData(
        it: List<UITask>
    ): MutableList<Hour> {
        val hourList = mutableListOf<Hour>()
        for (i in 0..23 step 1) {
            val listUITask = mutableListOf<UITask>()
            for (item in it) {
                if (item.eventHour == i) {
                    listUITask.add(item)
                }
            }
            hourList.add(Hour(i, ("$i" + "59").toInt(), listUITask))
        }
        return hourList
    }

    fun selectedTask(uuid: String){
        _selectedTask.value = uuid

    }

}