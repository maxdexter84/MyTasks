package ru.maxdexter.mytasks.repository.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.repository.LoadingResponse
import ru.maxdexter.mytasks.utils.Constants

object Auth {

     fun startAuth( activity: Activity){
        val providerList = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                .setAvailableProviders(providerList)
                .build(), Constants.RC_SIGN_IN
        )

    }

    fun outAuth(context: Context) =
          AuthUI.getInstance()
            .signOut(context)



}