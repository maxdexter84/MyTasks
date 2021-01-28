package ru.maxdexter.mytasks.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskFile
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import ru.maxdexter.mytasks.repository.localdatabase.TaskDao

class Repository(private val dao: TaskDao): LocalDatabase{

    override suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile) {
        withContext(Dispatchers.IO){
            dao.insertTaskWithTaskFile(taskWithTaskFile)
        }
    }

    override fun  getAllTask(): LiveData<List<Task>> {
       return dao.getAllTask()
    }

    override fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int): Flow<List<TaskWithTaskFile>> {
        return dao.getAllTaskWithTaskFile(year,month, day).flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTask(uuid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addTaskFile(taskFile: TaskFile) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskFile(taskFile: TaskFile) {
        TODO("Not yet implemented")
    }


}