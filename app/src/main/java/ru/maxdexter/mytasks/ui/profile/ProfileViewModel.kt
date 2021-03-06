package ru.maxdexter.mytasks.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.maxdexter.mytasks.domen.models.User
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.preferences.AppPreferences.Companion.IS_AUTH

class ProfileViewModel(private val appPreferences: AppPreferences) : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _isAuth = MutableLiveData<Boolean?>(null)
            val isAuth: LiveData<Boolean?>
            get() = _isAuth

    private val _currentTheme = MutableLiveData<Boolean>(false)
            val currentTheme: LiveData<Boolean>
            get() = _currentTheme
    init {
        _currentTheme.value = appPreferences.getTheme()
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
        return User(phone)

    }

    fun setTheme(isDarkTheme: Boolean){
        appPreferences.savePref(isDarkTheme,AppPreferences.IS_DARK_THEME)

    }



}