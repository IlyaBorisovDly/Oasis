package com.example.oasis.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oasis.ui.workout.Workout
import com.example.oasis.databinding.ActivityMainBinding
import com.example.oasis.ui.registration.RegistrationActivity
import com.example.oasis.ui.workout.WorkoutActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this, RegistrationActivity::class.java))

        binding.cardView1.setOnClickListener { startWorkout(Workout.FIRST) }
        binding.cardView2.setOnClickListener { startWorkout(Workout.SECOND) }
        binding.cardView3.setOnClickListener { startWorkout(Workout.THIRD) }
    }

    private fun startWorkout(workout: Workout) {
        val intent = Intent(this, WorkoutActivity::class.java)

        intent.putExtra("WorkoutEnum", workout)
        startActivity(intent)
    }
}