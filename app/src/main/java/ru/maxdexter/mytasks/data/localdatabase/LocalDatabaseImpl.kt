package ru.maxdexter.mytasks.data.localdatabase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile

class LocalDatabaseImpl(private val dao: TaskDao): ILocalDatabase {

    override suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile) {
        withContext(Dispatchers.IO){
            dao.insertTaskWithTaskFile(taskWithTaskFile)
        }
    }

    override fun  getAllTask(): Flow<List<TaskWithTaskFile>> {
       return dao.getAllTask().flowOn(Dispatchers.IO)
    }

    override fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int): Flow<List<TaskWithTaskFile>> {
        return dao.getAllTaskWithTaskFile(year,month, day).flowOn(Dispatchers.IO)
    }

    override fun getCurrentTask(uuid: String): LiveData<TaskWithTaskFile> {
        return dao.getTaskWithTaskFile(uuid)
    }

    override suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO){
            dao.deleteTask(task)
        }

    }


    override suspend fun deleteTaskFile(taskFile: TaskFile) {
        withContext(Dispatchers.IO){
            dao.deleteTaskFile(taskFile)
        }
    }


}