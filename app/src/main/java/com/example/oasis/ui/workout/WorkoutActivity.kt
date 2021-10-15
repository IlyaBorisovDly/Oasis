package com.example.oasis.ui.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var workoutViewModel: WorkoutViewModel

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

        val workoutNameTextView = binding.textViewWorkoutName
        val recyclerView: RecyclerView = binding.recyclerView

       workoutViewModel =
           ViewModelProvider(this, WorkoutViewModelFactory(application, workoutType))
               .get(WorkoutViewModel::class.java)

        workoutNameTextView.text = workoutViewModel.workoutName.value

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = WorkoutAdapter(workoutViewModel.exercisesList)
    }
}