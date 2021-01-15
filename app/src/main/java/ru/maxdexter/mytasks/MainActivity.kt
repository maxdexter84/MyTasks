package ru.maxdexter.mytasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import ru.maxdexter.mytasks.databinding.ActivityMainBinding
import ru.maxdexter.mytasks.repository.firebase.Auth
import ru.maxdexter.mytasks.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main )


    }


}

