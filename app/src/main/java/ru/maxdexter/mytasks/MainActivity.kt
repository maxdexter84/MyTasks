package ru.maxdexter.mytasks

import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.maxdexter.mytasks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main )
        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.navigation_home ->{
                    navController.navigate(MobileNavigationDirections.actionGlobalNavigationHome())
                    true
                }
                R.id.navigation_notifications -> {
                    navController.navigate(MobileNavigationDirections.actionGlobalNavigationNotifications())
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(MobileNavigationDirections.actionGlobalNavigationDashboard())
                    true
                }
                else -> false
            }
        }








    }
}