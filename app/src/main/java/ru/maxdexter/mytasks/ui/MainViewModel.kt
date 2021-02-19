package ru.maxdexter.mytasks.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.domen.models.*
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LoadingResponse
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.utils.taskFSToTaskWithTaskFile
import ru.maxdexter.mytasks.utils.taskWithTaskFileToTaskFS
import java.io.IOException

class MainViewModel(private val remoteRepository: RemoteDataProvider, private val localRepository: LocalDatabase, private val storage: DataStorage) : ViewModel() {
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
    init {
        _isAuth.value = checkAuth()
    }

    private fun checkAuth(): Boolean{
        val user =  remoteRepository.getCurrentUser()
        return user != null
     }

     fun getCurrentTaskList(){
        viewModelScope.launch {
            localRepository.getAllTask().collect { list->
                list.forEach {
                    if (!it.task.saveToCloud) {
                        saveTaskAndFile(it)
                    }
                }

            }
        }
    }

    private suspend fun saveTaskAndFile(taskWithTaskFile: TaskWithTaskFile){
        val taskFS = taskWithTaskFileToTaskFS(taskWithTaskFile)
        withContext(Dispatchers.IO){
            try {
                when(val response = storage.saveFileToStorage(taskWithTaskFile).value){
                    is LoadingResponse.Success<*> -> {
                        val result = response.data as List<TaskFile>
                        taskFS.userFilesCloudStorage = result
                        remoteRepository.saveTask(taskFS)
                        updateTaskWithTaskFile(taskFS)
                    }
                    is LoadingResponse.Error -> {
                        getCurrentTaskList()
                    }

                    else -> {Log.i("TAG","unknown state")}
                }
            }catch (e: IOException){
                Log.e("TAG","saveTaskAndFile ${e.stackTrace}")}
        }
    }

    private suspend fun updateTaskWithTaskFile(taskFS: TaskFS){
                val taskWithTaskFile = taskFSToTaskWithTaskFile(taskFS)
                taskWithTaskFile.task.saveToCloud = true
                localRepository.saveTask(taskWithTaskFile)
        }
    }









