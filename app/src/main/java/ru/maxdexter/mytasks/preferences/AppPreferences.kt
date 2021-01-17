package ru.maxdexter.mytasks.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AppPreferences(context: Context) {
    private val APP_PREF = "APP_PREF"
    private val MODE_PRIVATE = 0
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_PREF,MODE_PRIVATE)

    suspend fun getIsAuth(): Int{
        return  withContext(Dispatchers.IO){
            sharedPreferences.getInt(IS_AUTH, 0)
        }
    }
    suspend fun setIsAuth(isAuth: Int){
        withContext(Dispatchers.IO){
            sharedPreferences.edit().putInt(IS_AUTH,isAuth).apply()
        }
    }

    suspend fun getTheme(): Boolean {
       return withContext(Dispatchers.IO){
            sharedPreferences.getBoolean(IS_DARK_THEME,false)
        }
    }

    suspend fun setTheme(isDarkTheme: Boolean){
        withContext(Dispatchers.IO){
            sharedPreferences.edit().putBoolean(IS_DARK_THEME,isDarkTheme).apply()
        }
    }





    companion object{ 
        const val IS_AUTH = "is_auth"
        const val IS_DARK_THEME = "IS_DARK_THEME"
    }

}