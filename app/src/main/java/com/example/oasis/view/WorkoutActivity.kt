package com.example.oasis.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.Exercise
import com.example.oasis.ExerciseAdapter
import com.example.oasis.R
import com.example.oasis.Workout
import com.example.oasis.databinding.ActivityWorkoutBinding

class WorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = getIntent()
        val workout: Workout = intent.getSerializableExtra("WorkoutEnum") as Workout

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = getAdapter(workout)
    }

    private fun getAdapter(workout: Workout): ExerciseAdapter {
        when(workout) {
            Workout.FIRST -> {
                val ex1 = Exercise("Жим лёжа", 0.0)
                val ex2 = Exercise("Становая тяга", 0.0)
                val ex3 = Exercise("Жим гантелей", 0.0)
                val ex4 = Exercise("Пресс", 0.0)

                val array: ArrayList<Exercise> = arrayListOf(ex1, ex2, ex3, ex4)
                return ExerciseAdapter(array)
            }
            Workout.SECOND -> {
                val ex1 = Exercise("им лёжа", 0.0)
                val ex2 = Exercise("тановая тяга", 0.0)
                val ex3 = Exercise("им гантелей", 0.0)
                val ex4 = Exercise("ресс", 0.0)

                val array: ArrayList<Exercise> = arrayListOf(ex1, ex2, ex3, ex4)
                return ExerciseAdapter(array)
            }
            Workout.THIRD -> {
                val ex1 = Exercise("Жим лёжа", 10.0)
                val ex2 = Exercise("Становая тяга", 20.0)
                val ex3 = Exercise("Жим гантелей", 30.0)
                val ex4 = Exercise("Пресс", 50.0)

                val array: ArrayList<Exercise> = arrayListOf(ex1, ex2, ex3, ex4)
                return ExerciseAdapter(array)
            }
            else -> {
                val ex1 = Exercise("", 0.0)
                val array: ArrayList<Exercise> = arrayListOf(ex1)
                return ExerciseAdapter(array)
            }
        }

    }
}