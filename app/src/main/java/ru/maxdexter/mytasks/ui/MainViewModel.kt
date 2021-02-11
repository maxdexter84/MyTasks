package ru.maxdexter.mytasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.domen.repository.firebase.RemoteDataProviderImpl

class MainViewModel(private val remoteDataProvider: RemoteDataProvider) : ViewModel() {
    private val _isAuth = MutableLiveData<Boolean>(false)
            val iasAuth: LiveData<Boolean>
            get() = _isAuth

    init {
        _isAuth.value = checkAuth()
    }

        private fun checkAuth(): Boolean{
        val user =  remoteDataProvider.getCurrentUser()
        return user != null
    }
}