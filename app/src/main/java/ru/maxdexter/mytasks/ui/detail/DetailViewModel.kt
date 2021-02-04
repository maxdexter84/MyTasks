package ru.maxdexter.mytasks.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.utils.handleParseDate
import ru.maxdexter.mytasks.utils.handleParseTime
import java.util.*


class DetailViewModel(private val repository: LocalDatabase,  uuid: String) : ViewModel() {

    private val _currentTaskWithTaskFile = MutableLiveData<TaskWithTaskFile>()
            val currentTaskWithTaskFile: LiveData<TaskWithTaskFile>
            get() = _currentTaskWithTaskFile

    private val _taskDate = MutableLiveData<String>()
            val taskDate: LiveData<String>
            get() = _taskDate

    private val _taskTime = MutableLiveData<String>()
            val taskTime: LiveData<String>
            get() = _taskTime



    init {
        getTask(uuid)
    }


    private fun getTask(uuid: String){
        viewModelScope.launch {
            repository.getCurrentTask(uuid).collect {
                _currentTaskWithTaskFile.value = it
                _taskDate.value = handleParseDate(it)
                _taskTime.value = handleParseTime(it)
            }
        }
    }


}