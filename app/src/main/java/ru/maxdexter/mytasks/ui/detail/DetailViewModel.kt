package ru.maxdexter.mytasks.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.repository.LocalDatabase
import java.util.*


class DetailViewModel(private val repository: LocalDatabase,  uuid: String) : ViewModel() {

    private val _currentTaskWithTaskFile = MutableLiveData<TaskWithTaskFile>()
            val currentTaskWithTaskFile: LiveData<TaskWithTaskFile>
            get() = _currentTaskWithTaskFile

    init {
        getTask(uuid)
    }


    private fun getTask(uuid: String){
        viewModelScope.launch {
            repository.getCurrentTask(uuid).collect {
                _currentTaskWithTaskFile.value = it
            }
        }
    }
}