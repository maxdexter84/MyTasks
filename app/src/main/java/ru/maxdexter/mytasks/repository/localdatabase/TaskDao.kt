package ru.maxdexter.mytasks.repository.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.models.Task
import ru.maxdexter.mytasks.models.TaskFile
import ru.maxdexter.mytasks.models.TaskWithTaskFile
import java.time.Year

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Transaction @Query("SELECT * FROM task WHERE eventYear = :year AND eventMonth = :month AND eventDay = :day")
    fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int) : Flow<List<TaskWithTaskFile>>

    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskFile(file:TaskFile)

    @Delete
    fun deleteTaskFile(file: TaskFile)

    @Transaction
    suspend fun insertTaskWithTaskFile(taskWithTaskFile: TaskWithTaskFile){
        taskWithTaskFile.task?.let { addTask(it) }
        taskWithTaskFile.list?.forEach { addTaskFile(it) }
    }
}