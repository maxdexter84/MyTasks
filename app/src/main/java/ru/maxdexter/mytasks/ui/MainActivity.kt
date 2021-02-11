package ru.maxdexter.mytasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.koin.experimental.property.inject
import ru.maxdexter.mytasks.MobileNavigationDirections
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.ActivityMainBinding
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.domen.repository.firebase.RemoteDataProviderImpl
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.utils.INTENT_TASK_UUID
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.maxdexter.mytasks.domen.repository.firebase.Auth

@KoinApiExtension
class MainActivity : AppCompatActivity(), KoinComponent {
    lateinit var navHost: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private val appPreferences by lazy {
        AppPreferences(this)
    }
    lateinit var navController: NavController

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



    }




}

