package ru.maxdexter.mytasks.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskFile
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import java.util.*


interface LocalDatabase {

   suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile)

    fun getAllTask() : LiveData<List<Task>>
    fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int) : Flow<List<TaskWithTaskFile>>

   suspend fun deleteTask(uuid: String)

   suspend fun addTaskFile(taskFile: TaskFile)

   suspend fun deleteTaskFile(taskFile: TaskFile)
}