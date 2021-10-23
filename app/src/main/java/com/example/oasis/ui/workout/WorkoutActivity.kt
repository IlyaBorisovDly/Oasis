package com.example.oasis.ui.workout

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oasis.WorkoutType
import com.example.oasis.databinding.ActivityWorkoutBinding
import kotlinx.coroutines.*

@InternalCoroutinesApi
class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var workoutType: WorkoutType
    private lateinit var workoutViewModel: WorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        workoutType =
            if (bundle?.get("WorkoutType") is WorkoutType) {
                bundle.get("WorkoutType") as WorkoutType
            } else {
                WorkoutType.FIRST
            }

        workoutViewModel =
            ViewModelProvider(this, WorkoutViewModelFactory(application, workoutType))
                .get(WorkoutViewModel::class.java)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val listener = getListener()

        workoutViewModel.exercisesList.observe(this, {
            recyclerView.adapter = WorkoutAdapter(it, listener)
        })
    }

    override fun onBackPressed() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder
            .setTitle("Действительно выйти?")
            .setMessage("Прогресс не будет сохранён")
            .setCancelable(true)
            .setNegativeButton("Остаться") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Выйти") { dialog, _ ->
                dialog.dismiss()
                finish()
            }

        dialogBuilder.show()
    }

    private fun getListener(): Listener {
        return object : Listener {

            override fun onResultsApplied(map: Map<String, Int>) {
                workoutViewModel.updateBestResults(map, workoutType)
            }

        }
    }

    interface Listener {
        fun onResultsApplied(map: Map<String, Int>)
    }
}