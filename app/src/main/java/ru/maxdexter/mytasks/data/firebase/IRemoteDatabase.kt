package ru.maxdexter.mytasks.data.firebase

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.data.firebase.entity.TaskFS
import ru.maxdexter.mytasks.data.firebase.entity.User
import ru.maxdexter.mytasks.domen.common.LoadingResponse

interface IRemoteDatabase {
    fun getUserTasksCollection(): CollectionReference
    fun getCurrentUser(): User?
    suspend fun saveAllTasks(tasks: List<TaskFS>): LoadingResponse
    suspend fun saveTask(task: TaskFS)
    suspend fun deleteTask(task: TaskFS): LoadingResponse
    suspend fun getAllTask(): Flow<LoadingResponse>
    suspend fun getTaskByUUID(uuid: String) : LoadingResponse
}