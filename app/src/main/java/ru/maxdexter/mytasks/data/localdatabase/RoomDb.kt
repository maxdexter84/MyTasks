package ru.maxdexter.mytasks.data.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile

@Database(entities = [Task::class, TaskFile::class], version = 1,exportSchema = false)
abstract class RoomDb: RoomDatabase() {
   abstract fun getDao():TaskDao


    companion object{
        @Volatile
        private var instance: RoomDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context,
            RoomDb::class.java,
            "app_db.db").build()
    }
}