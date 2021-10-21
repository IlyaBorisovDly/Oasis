package com.example.oasis.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.oasis.WorkoutType
import com.example.oasis.databinding.ActivityMainBinding
import com.example.oasis.ui.login.LoginActivity
import com.example.oasis.ui.settings.SettingsActivity
import com.example.oasis.ui.workout.WorkoutActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardView1.setOnClickListener { startWorkout(WorkoutType.FIRST) }
        binding.cardView2.setOnClickListener { startWorkout(WorkoutType.SECOND) }
        binding.cardView3.setOnClickListener { startWorkout(WorkoutType.THIRD) }

        binding.profileImageView.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    private fun startWorkout(workoutType: WorkoutType) {
        val intent = Intent(this, WorkoutActivity()::class.java)

        intent.putExtra("WorkoutType", workoutType)
        startActivity(intent)
    }
}