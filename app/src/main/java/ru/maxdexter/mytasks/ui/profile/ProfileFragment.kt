package ru.maxdexter.mytasks.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.FragmentProfileBinding
import ru.maxdexter.mytasks.domen.repository.firebase.Auth
import ru.maxdexter.mytasks.preferences.AppPreferences

class ProfileFragment : BottomSheetDialogFragment() {

    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var binding: FragmentProfileBinding
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        isDarkTheme = AppPreferences(requireContext()).getTheme()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile, container, false)
        authObserver()
        initBtn()
        profileViewModel.currentTheme.observe(viewLifecycleOwner,{
            binding.switchAppTheme.isChecked = it
        })

       return binding.root
    }





    private fun authObserver() {
        profileViewModel.isAuth.observe(viewLifecycleOwner, {
            it?.let { it1 -> updateUi(it1) }
        })
    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        profileViewModel.getUserData()
        setTheme()
    }

    private fun setTheme() {

        binding.switchAppTheme.isChecked = isDarkTheme
        binding.switchAppTheme.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.setTheme(isChecked)
            requireActivity().recreate()
        }
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