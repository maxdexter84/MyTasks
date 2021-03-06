package ru.maxdexter.mytasks.domen.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile


interface LocalDatabase {

   suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile)

    fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int) : Flow<List<TaskWithTaskFile>>
    fun getAllTask() : Flow<List<TaskWithTaskFile>>
    fun getTaskWithTaskFile(uuid: String) : Flow<TaskWithTaskFile>

    fun getCurrentTask(uuid: String): Flow<TaskWithTaskFile>

   suspend fun deleteTask(task: Task)

   suspend fun addTaskFile(taskFile: TaskFile)

   suspend fun deleteTaskFile(taskFile: TaskFile)

}