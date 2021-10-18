package com.example.oasis.ui.workout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.model.Exercise
import com.example.oasis.model.Workout
import org.json.JSONArray
import org.json.JSONObject

class WorkoutViewModelFactory(
    private val application: Application,
    private val workoutType: WorkoutType
    ): ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WorkoutViewModel(application, workoutType) as T
    }
}