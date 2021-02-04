package ru.maxdexter.mytasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxdexter.mytasks.databinding.ActivityMainBinding
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.repository.firebase.Auth
import ru.maxdexter.mytasks.utils.INTENT_TASK_UUID
import ru.maxdexter.mytasks.utils.REQUEST_CODE
import java.util.*

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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main )
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val intent = intent.extras?.get(INTENT_TASK_UUID)
        if (intent != null){
            navController.navigate(MobileNavigationDirections.actionGlobalCalendarFragment(intent.toString()))
        }

    }


}

