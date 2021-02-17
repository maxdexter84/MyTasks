package ru.maxdexter.mytasks.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
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

    init {
        _isAuth.value = checkAuth()
    }

        private fun checkAuth(): Boolean{
        val user =  remoteRepository.getCurrentUser()
        return user != null
    }





    @KoinApiExtension
    private fun saveTaskToDb(taskWithTaskFile: TaskWithTaskFile){

        try {
            if (isOnline){
                viewModelScope.launch {
                    taskWithTaskFile.task.saveToCloud = true
                    localRepository.saveTask(taskWithTaskFile)
                }
                saveTaskToFireStore(taskWithTaskFile)
                saveFileToStorage(taskWithTaskFile)

            }else {
                viewModelScope.launch {
                    taskWithTaskFile.task.saveToCloud = false
                    localRepository.saveTask(taskWithTaskFile)
                }
            }

            if (taskWithTaskFile.task.pushMessage){

                _setAlarm.value = taskWithTaskFile.task
            }

        }catch (e: IOException) {
            Log.e("ERROR_SAVE",e.message.toString())
        }



    }

    private  fun saveTaskToFireStore(taskWithTaskFile: TaskWithTaskFile) {
        GlobalScope.launch {
            remoteRepository.saveTask(taskWithTaskFileToTaskFS(taskWithTaskFile))
        }
    }


    private fun saveFileToStorage(taskWithTaskFile: TaskWithTaskFile){
        if (taskWithTaskFile.list.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                storage.saveFileToStorage(taskWithTaskFile)
            }
        }
    }


    fun deleteTask(taskWithTaskFile: TaskWithTaskFile){
        viewModelScope.launch {
            localRepository.deleteTask(taskWithTaskFile.task)
            taskWithTaskFile.list.let { list1 ->
                list1.forEach { file->
                    file.let { localRepository.deleteTaskFile(it) }
                } }

        }
    }

}