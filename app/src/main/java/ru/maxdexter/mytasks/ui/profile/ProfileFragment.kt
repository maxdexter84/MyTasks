package ru.maxdexter.mytasks.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentProfileBinding
import ru.maxdexter.mytasks.models.User
import ru.maxdexter.mytasks.repository.firebase.Auth
import ru.maxdexter.mytasks.utils.Constants

class ProfileFragment : BottomSheetDialogFragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private var isAuth:Boolean? = false
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile, container, false)
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)


        initBtn()


        profileViewModel.currentUser.observe(viewLifecycleOwner,{
            when(it?.isAnonymous){
                true ->{
                    binding.btnOut.isEnabled = false
                    binding.btnIn.isEnabled = true
                    binding.btnSync.isEnabled = false
                }
                false  -> {
                    binding.btnOut.isEnabled = true
                    binding.btnIn.isEnabled = false
                    binding.btnSync.isEnabled = true
                }
            }
        })


       return binding.root
    }

    private fun initBtn() {
        binding.btnIn.setOnClickListener {
            activity?.let { act ->
                Auth.startAuth(act)
                profileViewModel.getUserData()}
        }
        binding.btnOut.setOnClickListener {
            context?.let { con ->
                Auth.outAuth(con)
                profileViewModel.getUserData()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                isAuth = true
                Log.i("LOGIN_IN_PROFILE","${user?.email} ${user?.displayName} ${user?.photoUrl}")
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}