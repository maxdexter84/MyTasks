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
    suspend fun saveAllTasks(tasks: List<TaskFS>):LoadingResponse
    suspend fun saveTask(task: TaskFS)
    suspend fun deleteTask(task: TaskFS):LoadingResponse
    suspend fun getAllTask(): Flow<LoadingResponse>
    suspend fun getTaskByUUID(uuid: String) : LoadingResponse
}