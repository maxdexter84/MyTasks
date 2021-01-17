package ru.maxdexter.mytasks.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentProfileBinding
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.repository.firebase.Auth

class ProfileFragment : BottomSheetDialogFragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileViewModelFactory: ProfileViewModelFactory
    private lateinit var binding: FragmentProfileBinding
    lateinit var appPreferences: AppPreferences
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile, container, false)
        appPreferences = AppPreferences(requireContext())
        initViewModel()
        authObserver()
        initBtn()
        binding.btnSync.addOnCheckedChangeListener { button, isChecked ->  }
       return binding.root
    }

    internal fun initViewModel() {
        profileViewModelFactory = ProfileViewModelFactory(appPreferences)
        profileViewModel =
            ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)
    }

    private fun authObserver() {
        profileViewModel.isAuth.observe(viewLifecycleOwner, {
            it?.let { it1 -> updateUi(it1) }
        })
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getUserData()
    }

    private fun initBtn() {
        binding.btnIn.setOnClickListener {
            Auth.startAuth(requireActivity())
            profileViewModel.getUserData()
        }
        binding.btnOut.setOnClickListener {
            Auth.outAuth(requireContext()).addOnSuccessListener {
                profileViewModel.getUserData()
            }
        }
    }






    private fun updateUi(requestCode: Boolean){
        when(requestCode){
           false ->{
                binding.btnOut.isEnabled = false
                binding.btnIn.isEnabled = true
                binding.btnSync.isEnabled = false
            }
            true  -> {
                binding.btnOut.isEnabled = true
                binding.btnIn.isEnabled = false
                binding.btnSync.isEnabled = true
            }
        }
    }
}