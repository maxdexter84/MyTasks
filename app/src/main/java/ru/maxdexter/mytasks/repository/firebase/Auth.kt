package ru.maxdexter.mytasks.repository.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import ru.maxdexter.mytasks.utils.Constants

object Auth {
     fun startAuth( activity: Activity){
        val providerList = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build())

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providerList)
                .build(), Constants.RC_SIGN_IN
        )

    }

}