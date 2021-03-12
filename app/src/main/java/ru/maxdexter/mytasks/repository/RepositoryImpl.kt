package ru.maxdexter.mytasks.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.maxdexter.mytasks.data.firebase.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import ru.maxdexter.mytasks.data.firebase.IRemoteDatabase
import ru.maxdexter.mytasks.data.localdatabase.ILocalDatabase
import ru.maxdexter.mytasks.domen.common.IRepository
import ru.maxdexter.mytasks.domen.extension.mapToTaskWithTaskFile
import ru.maxdexter.mytasks.domen.extension.mapToUITask
import ru.maxdexter.mytasks.ui.entity.UITask

class RepositoryImpl(private val localDatabase: ILocalDatabase, private val remoteDatabase: IRemoteDatabase) : IRepository {

    override fun getAllTasks(): Flow<List<UITask>> {
        TODO("Not yet implemented")
    }

    override fun getCurrentDateTasks(year: Int, month: Int, day: Int): Flow<List<UITask>> {
        return localDatabase.getAllTaskWithTaskFile(year, month, day).map { it.map { taskWithTaskFile -> taskWithTaskFile.mapToUITask() }}
    }

    override fun getTaskFromID(uuid: String): LiveData<UITask> {
        return localDatabase.getCurrentTask(uuid).map { it.mapToUITask() }
    }

    override suspend fun deleteTask(task: UITask) {
        val taskWithFile = task.mapToTaskWithTaskFile()
       localDatabase.deleteTask(taskWithFile.task)
        if (taskWithFile.list.isNotEmpty()){
            taskWithFile.list.onEach {
                localDatabase.deleteTaskFile(it)
            }
        }
    }

    override suspend fun saveTask(task: UITask) {
        localDatabase.saveTask(task.mapToTaskWithTaskFile())
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = remoteDatabase.getCurrentUser()
        if (firebaseUser != null){
           return User(firebaseUser.phone)
        }
       return null

    }
}