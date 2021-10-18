package com.example.oasis.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oasis.data.Repository
import com.example.oasis.model.Exercise
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application, private val workoutType: WorkoutType) : AndroidViewModel(application) {

    var exercisesList = MutableLiveData<List<Exercise>>().apply { value = mutableListOf() }

    fun getBestResults(workoutType: WorkoutType) = Repository.getBestResults(workoutType)

    fun updateBestResults(map: Map<String, Int>, workoutType: WorkoutType) {
        viewModelScope.launch {
            Repository.updateBestResults(map, workoutType)
        }
    }
}