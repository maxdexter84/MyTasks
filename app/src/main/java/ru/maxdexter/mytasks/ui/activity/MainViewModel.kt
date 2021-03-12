package ru.maxdexter.mytasks.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile
import ru.maxdexter.mytasks.data.firebase.IDataStorage
import ru.maxdexter.mytasks.data.localdatabase.ILocalDatabase
import ru.maxdexter.mytasks.data.firebase.IRemoteDatabase
import ru.maxdexter.mytasks.domen.common.IRepository
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.IOException

class MainViewModel(private val repository: IRepository, private val storageI: IDataStorage) : ViewModel() {
    var isOnline: Boolean = true
    private val _setAlarm = MutableLiveData<Task>()
            val setAlarm: LiveData<Task>
            get() = _setAlarm

    private val _isAuth = MutableLiveData<Boolean>(false)
            val iasAuth: LiveData<Boolean>
            get() = _isAuth

    private val _taskList = MutableLiveData<List<TaskWithTaskFile>>()
            val taskList: LiveData<List<TaskWithTaskFile>>
            get() = _taskList

    private val _dataToSync = MutableLiveData<List<TaskWithTaskFile>>()
            val dataToSync: LiveData<List<TaskWithTaskFile>>
            get() = _dataToSync


    init {
        _isAuth.value = checkAuth()
    }






    private fun checkAuth(): Boolean{
        val user =  repository.getCurrentUser()
        return user != null
    }


}









