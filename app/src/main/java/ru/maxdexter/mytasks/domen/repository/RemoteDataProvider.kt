package ru.maxdexter.mytasks.domen.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.inject.Deferred
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFS
import ru.maxdexter.mytasks.domen.models.User

interface RemoteDataProvider {
    fun getUserTasksCollection(): CollectionReference
    fun getCurrentUser(): User?
    suspend  fun<T> saveAllTasks(tasks: List<TaskFS>):LoadingResponse<T>
    suspend fun saveTask(task: TaskFS)
    suspend fun<T>deleteTask(task: TaskFS):LoadingResponse<T>
    suspend fun <T>getAllTask(): Flow<LoadingResponse<T>>
    suspend fun <T>getTaskByUUID(uuid: String) : LoadingResponse<T>
}