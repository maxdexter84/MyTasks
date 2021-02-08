package ru.maxdexter.mytasks.domen.repository

import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.domen.models.Task

interface RemoteDataProvider {
    fun saveAllTasks(tasks: List<Task>):Flow<Boolean>
    fun saveTask(task: Task): Flow<Boolean>
    fun deleteTask(task: Task):Flow<Boolean>
    fun <T>getAllTask(): Flow<LoadingResponse<T>>
    fun <T>getTaskByUUID(uuid: String) : Flow<LoadingResponse<T>>
}