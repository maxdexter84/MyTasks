package ru.maxdexter.mytasks.domen.repository.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.maxdexter.mytasks.domen.models.Task
import ru.maxdexter.mytasks.domen.models.TaskFile
import ru.maxdexter.mytasks.domen.models.TaskWithTaskFile

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Transaction @Query("SELECT * FROM task WHERE eventYear = :year AND eventMonth = :month AND eventDay = :day")
    fun getAllTaskWithTaskFile(year: Int, month: Int, day: Int) : Flow<List<TaskWithTaskFile>>


    @Transaction @Query("SELECT * FROM task")
    fun getAllTask() : Flow<List<TaskWithTaskFile>>

    @Transaction @Query("SELECT * FROM task WHERE id =:uuid ")
    fun getTaskWithTaskFile(uuid: String) : Flow<TaskWithTaskFile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTaskFile(file:TaskFile)

    @Delete
    fun deleteTaskFile(file: TaskFile)

    @Transaction
    suspend fun insertTaskWithTaskFile(taskWithTaskFile: TaskWithTaskFile){
        addTask(taskWithTaskFile.task)
        taskWithTaskFile.list.forEach { addTaskFile(it) }
    }
}