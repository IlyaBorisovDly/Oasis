package com.example.oasis.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.oasis.ui.workout.WorkoutType
import com.example.oasis.databinding.ActivityMainBinding
import com.example.oasis.ui.login.LoginActivity
import com.example.oasis.ui.settings.SettingsActivity
import com.example.oasis.ui.workout.WorkoutActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var workoutCard1: CardView
    private lateinit var workoutCard2: CardView
    private lateinit var workoutCard3: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        workoutCard1 = binding.cardView1
        workoutCard2 = binding.cardView2
        workoutCard3 = binding.cardView3

        workoutCard1.setOnClickListener { startWorkout(WorkoutType.FIRST) }
        workoutCard2.setOnClickListener { startWorkout(WorkoutType.SECOND) }
        workoutCard3.setOnClickListener { startWorkout(WorkoutType.THIRD) }

        binding.profileImageView.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun startWorkout(workoutType: WorkoutType) {
        val intent = Intent(this, WorkoutActivity()::class.java)
        intent.putExtra("WorkoutType", workoutType)
        startActivity(intent)
    }
}