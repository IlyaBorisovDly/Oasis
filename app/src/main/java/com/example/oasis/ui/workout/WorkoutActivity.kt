package com.example.oasis.ui.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.State
import com.example.oasis.databinding.ActivityWorkoutBinding
import com.example.oasis.model.Exercise
import com.example.oasis.utils.showToast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
class WorkoutActivity : AppCompatActivity() {

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var recyclerView: RecyclerView


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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        workoutViewModel =
           ViewModelProvider(this, WorkoutViewModelFactory(application, workoutType))
               .get(WorkoutViewModel::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            getExercises()
        }
    }

    private suspend fun getExercises() {
        workoutViewModel.getAllBestResults().collect { state ->
            when (state) {
                is State.Loading -> {
                    recyclerView.adapter = WorkoutAdapter(workoutViewModel.exercisesList.value!!)
                    showToast("Loading...")
                }
                is State.Success -> {
                    recyclerView.adapter = WorkoutAdapter(getData(state.data))
                }
                is State.Failed -> {
                    showToast("Failed...")
                }
            }
        }
    }

    private fun getData(map: Map<String, Int>): List<Exercise> {
        val exercisesNames = workoutViewModel.getExercisesNames()
        val list = mutableListOf<Exercise>()

        for (i in exercisesNames.indices) {
            val name = exercisesNames[i]
            val bestResult = map.getValue(name)
            val exercise = Exercise(name, bestResult)

            list.add(exercise)
        }

        return list
    }
}