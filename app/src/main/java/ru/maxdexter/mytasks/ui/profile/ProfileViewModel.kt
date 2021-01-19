package ru.maxdexter.mytasks.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.preferences.AppPreferences.Companion.IS_AUTH
import ru.maxdexter.mytasks.preferences.AppPreferences.Companion.IS_DARK_THEME

class ProfileViewModel(private val appPreferences: AppPreferences) : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _isAuth = MutableLiveData<Boolean?>(null)
            val isAuth: LiveData<Boolean?>
            get() = _isAuth
    init {
        getUserData()
    }

    fun getUserData() {
        val user = FirebaseAuth.getInstance().currentUser
        val res = user != null
        _isAuth.value = res
        appPreferences.savePref(res, IS_AUTH)
        if (user != null){
            _currentUser.value = handleMapFirebaseUser(user)
        }
    }

    private fun handleMapFirebaseUser(it: FirebaseUser): User {
        val phone = it.phoneNumber.toString()
         val user = User()
        user.phone = phone
        return user

    }



}