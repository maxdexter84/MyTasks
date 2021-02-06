package ru.maxdexter.mytasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.maxdexter.mytasks.MobileNavigationDirections
import ru.maxdexter.mytasks.R
import ru.maxdexter.mytasks.databinding.ActivityMainBinding
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.utils.INTENT_TASK_UUID

class MainActivity : AppCompatActivity() {
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

        val intent = intent.extras?.get(INTENT_TASK_UUID)
        if (intent != null){
            navController.navigate(MobileNavigationDirections.actionGlobalCalendarFragment(intent.toString()))
        }

    }


}

