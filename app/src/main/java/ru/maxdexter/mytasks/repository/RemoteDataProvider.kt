package ru.maxdexter.mytasks.repository

import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.User

interface RemoteDataProvider {

    fun saveTask(task: Task): Flow<Boolean>
    fun deleteTask(task: Task):Flow<Boolean>
    fun <T>getAllTask(): Flow<LoadingResponse<T>>
    fun <T>getTaskByUUID(uuid: String) : Flow<LoadingResponse<T>>
}