package ru.maxdexter.mytasks.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxdexter.mytasks.repository.LocalDatabase

class DetailViewModelFactory(private val repository: LocalDatabase, private val uuid: String) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository, uuid) as T
    }
}