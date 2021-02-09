package ru.maxdexter.mytasks.domen.repository.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.firebase.ui.auth.AuthUI
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.utils.RC_SIGN_IN
import java.lang.Exception

object Auth {

     fun startAuth( activity: Activity){
        val providerList = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
            try {
                activity.startActivityForResult(

                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(providerList)
                        .build(), RC_SIGN_IN
                )
            }catch (e:Exception){
                e.message?.let { Log.e("AUTH_ERROR", it) }
            }


    }

    fun outAuth(context: Context) =
          AuthUI.getInstance()
            .signOut(context)
}