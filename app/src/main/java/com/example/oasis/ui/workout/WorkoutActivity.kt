package com.example.oasis.ui.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        val workoutType: WorkoutType =
            if (bundle?.get("WorkoutType") is WorkoutType) {
                bundle.get("WorkoutType") as WorkoutType
            } else {
                WorkoutType.FIRST
            }

        val recyclerView: RecyclerView = binding.recyclerView
        val workoutNameTextView = binding.textViewWorkoutName

        val workout = WorkoutFactory.createWorkout(application, workoutType)

        workoutNameTextView.text = workout.name

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WorkoutAdapter(workout.exercises)
    }
}