package ru.maxdexter.mytasks.ui.newtask

import android.app.AlarmManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository

class NewTaskViewModelFactory(private val repository: LocalDatabase, private val alarmManager: AlarmManager, private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewTaskViewModel(repository, alarmManager, context) as T
    }
}