package ru.maxdexter.mytasks.data.localdatabase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile


interface ILocalDatabase {

   suspend fun saveTask(taskWithTaskFile: TaskWithTaskFile)

    fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int) : Flow<List<TaskWithTaskFile>>
    fun getAllTask() : Flow<List<TaskWithTaskFile>>
    fun getCurrentTask(uuid: String): LiveData<TaskWithTaskFile>
   suspend fun deleteTask(task: Task)
   suspend fun deleteTaskFile(taskFile: TaskFile)

}