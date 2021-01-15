package ru.maxdexter.mytasks.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.repository.firebase.Auth

class ProfileViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
            val currentUser: LiveData<User>
            get() = _currentUser

    init {
        getUserData()
    }
    fun getUserData(){
        viewModelScope.launch {
            Auth.getCurrentUser().collect {
                if (it != null){
                    val name = it.displayName
                    val email = it.email
                    val phone = it.phoneNumber
                    val photo = it.photoUrl
                    val isAnonymous = it.isAnonymous
                    val user = User(
                        name,
                        email,phone,
                        photo,
                        isAnonymous)
                    _currentUser.value = user
                    }
                }

            }
        }

    }
