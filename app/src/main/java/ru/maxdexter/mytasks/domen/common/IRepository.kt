package ru.maxdexter.mytasks.domen.common

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.data.firebase.entity.User
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile
import ru.maxdexter.mytasks.ui.entity.UITask

interface IRepository {
    fun getAllTasks(): Flow<List<UITask>>
    fun getCurrentDateTasks(year: Int, month: Int, day: Int): Flow<List<UITask>>
    fun getTaskFromID(uuid: String): LiveData<UITask>
    suspend fun deleteTask(task: UITask)
    suspend fun saveTask(task: UITask)
    fun getCurrentUser(): User?
}