package ru.maxdexter.mytasks.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.maxdexter.mytasks.preferences.AppPreferences

class ProfileViewModelFactory(private val appPreferences: AppPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(appPreferences) as T
    }
}