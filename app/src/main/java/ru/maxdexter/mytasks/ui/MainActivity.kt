package ru.maxdexter.mytasks.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Data
import ru.maxdexter.mytasks.MobileNavigationDirections
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.ActivityMainBinding
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.utils.INTENT_TASK_UUID
import org.koin.android.viewmodel.ext.android.viewModel
import ru.maxdexter.mytasks.domen.repository.firebase.Auth
import ru.maxdexter.mytasks.utils.CheckNetwork
import ru.maxdexter.mytasks.workmanager.WorkerUpload


class MainActivity : AppCompatActivity() {
    lateinit var navHost: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private val appPreferences by lazy {
        AppPreferences(this)
    }
    lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        when(appPreferences.getTheme()){
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val taskUUID = intent.extras?.get(INTENT_TASK_UUID)
        if (taskUUID != null){
            navController.navigate(MobileNavigationDirections.actionGlobalCalendarFragment(taskUUID.toString()))
        }

        viewModel.iasAuth.observe(this, {
            when(it){
                false-> Auth.startAuth(this)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        CheckNetwork(this).observe(this,{
            viewModel.isOnline = it
        })
    }






}

