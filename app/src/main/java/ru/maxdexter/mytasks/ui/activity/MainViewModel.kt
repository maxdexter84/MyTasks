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
import ru.maxdexter.mytasks.repository.DataStorage
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.loadstatus.LoadToCloudStatus
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.IOException

class MainViewModel(private val remoteDatabase: RemoteDataProvider, private val localDatabase: LocalDatabase, private val storage: DataStorage) : ViewModel() {
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



    fun getCurrentTaskList(){
        viewModelScope.launch(Dispatchers.IO) {
            localDatabase.getAllTask().collect { list->
                _dataToSync.postValue(list.filter { !it.task.saveToCloud })
            }
        }

    }

    fun startSaveToCloud(list: List<TaskWithTaskFile>){
        if (isOnline && checkAuth()){
            list.forEach {
                viewModelScope.launch {
                    saveTaskAndFile(it)
                }
            }
        }
    }


    private fun checkAuth(): Boolean{
        val user =  remoteDatabase.getCurrentUser()
        return user != null
    }

    private suspend fun saveTaskAndFile(taskWithTaskFile: TaskWithTaskFile){
        withContext(Dispatchers.IO){
            try {
                if (taskWithTaskFile.list.isNotEmpty()){
                    storage.saveFileToStorage(taskWithTaskFile.list,taskWithTaskFile.task.userNumber).collect {
                        when(it){
                            is LoadToCloudStatus.Success -> {
                                val taskFS = taskWithTaskFileToTaskFS(taskWithTaskFile)
                                taskFS.userFilesCloudStorage = it.data
                                remoteDatabase.saveTask(taskFS)
                                updateTaskWithTaskFile(taskWithTaskFile)
                            }
                            is LoadToCloudStatus.Error -> {
                                getCurrentTaskList()
                            }
                            else -> {Log.i("TAG","unknown state")}
                        }
                    }
                }else {
                    val taskFS = taskWithTaskFileToTaskFS(taskWithTaskFile)
                    remoteDatabase.saveTask(taskFS)
                    updateTaskWithTaskFile(taskWithTaskFile)
                }


            }catch (e: IOException){
                Log.e("TAG","saveTaskAndFile ${e.stackTrace}")}
        }
    }


    private suspend fun updateTaskWithTaskFile(taskWithTaskFile: TaskWithTaskFile){
        val task = taskWithTaskFile
        task.task.saveToCloud = true
        localDatabase.saveTask(task)
    }

}









