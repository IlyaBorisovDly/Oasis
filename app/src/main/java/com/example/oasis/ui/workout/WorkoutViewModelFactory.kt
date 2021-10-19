package com.example.oasis.ui.workout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.WorkoutType
import java.lang.NullPointerException

class WorkoutViewModelFactory(private val application: Application, private val workoutType: WorkoutType):
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == WorkoutViewModel::class.java) {
            return WorkoutViewModel(application, workoutType) as T
        } else {
            throw NullPointerException("WorkoutViewModelFactory: Passed wrong class")
        }
    }
}