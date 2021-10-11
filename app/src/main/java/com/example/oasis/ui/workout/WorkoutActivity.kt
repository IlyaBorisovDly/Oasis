package com.example.oasis.ui.workout

import android.content.Intent
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

        val intent: Intent = intent
        val workout: Workout = intent.getSerializableExtra("WorkoutEnum") as Workout

        val recyclerView: RecyclerView = binding.recyclerView
        // TODO: WorkoutFactory должен возвращать Workout
        val exercisesList = WorkoutFactory.createWorkout(application, workout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WorkoutAdapter(exercisesList)
    }
}