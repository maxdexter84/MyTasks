package ru.maxdexter.mytasks.ui.newtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxdexter.mytasks.repository.LocalDatabase
import ru.maxdexter.mytasks.repository.Repository

class NewTaskViewModelFactory(private val repository: LocalDatabase): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewTaskViewModel(repository) as T
    }
}