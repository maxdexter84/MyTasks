package ru.maxdexter.mytasks.domen.repository.firebase

import android.app.Activity
import android.content.Context
import com.firebase.ui.auth.AuthUI
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.utils.RC_SIGN_IN

object Auth {

     fun startAuth( activity: Activity){
        val providerList = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                .setAvailableProviders(providerList)
                .build(), RC_SIGN_IN
        )

    }

    fun outAuth(context: Context) =
          AuthUI.getInstance()
            .signOut(context)



}