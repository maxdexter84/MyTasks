package ru.maxdexter.mytasks.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AppPreferences(context: Context) {
    private val APP_PREF = "APP_PREF"
    private val MODE_PRIVATE = 0
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_PREF,MODE_PRIVATE)

     fun getIsAuth() =
        sharedPreferences.getBoolean(IS_AUTH, false)



    private  fun setIsAuth(isAuth: Boolean){
            sharedPreferences.edit().putBoolean(IS_AUTH,isAuth).apply()
    }

     fun getTheme() = sharedPreferences.getBoolean(IS_DARK_THEME,false)


    private  fun setTheme(isDarkTheme: Boolean){
            sharedPreferences.edit().putBoolean(IS_DARK_THEME,isDarkTheme).apply()
    }


    fun <T>savePref(tools: T, namePref: String){
            when(namePref){
                IS_AUTH -> setIsAuth(tools as Boolean)
                IS_DARK_THEME -> setTheme(tools as Boolean)
            }
    }


    companion object{ 
        const val IS_AUTH = "is_auth"
        const val IS_DARK_THEME = "IS_DARK_THEME"
    }

}