package ru.maxdexter.mytasks.domen.repository.localdatabase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile
import ru.maxdexter.mytasks.domen.repository.LocalDatabase

class LocalDatabaseImpl(private val database: RoomDb): LocalDatabase {

    override suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile) {
        withContext(Dispatchers.IO){
            database.getDao().insertTaskWithTaskFile(taskWithTaskFile)
        }
    }

    override fun  getAllTask(): LiveData<List<Task>> {
       return database.getDao().getAllTask()
    }

    override fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int): Flow<List<TaskWithTaskFile>> {
        return database.getDao().getAllTaskWithTaskFile(year,month, day).flowOn(Dispatchers.IO)
    }

    override fun getTaskWithTaskFile(uuid: String): Flow<TaskWithTaskFile> {
       return database.getDao().getTaskWithTaskFile(uuid).flowOn(Dispatchers.IO)
    }

    override fun getCurrentTask(uuid: String): Flow<TaskWithTaskFile> {
        return database.getDao().getTaskWithTaskFile(uuid).flowOn(Dispatchers.IO)
    }

    override suspend fun deleteTask(task:Task) {
        withContext(Dispatchers.IO){
            database.getDao().deleteTask(task)
        }

    }

    override suspend fun addTaskFile(taskFile: TaskFile) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskFile(taskFile: TaskFile) {
        withContext(Dispatchers.IO){
            database.getDao().deleteTaskFile(taskFile)
        }
    }


}