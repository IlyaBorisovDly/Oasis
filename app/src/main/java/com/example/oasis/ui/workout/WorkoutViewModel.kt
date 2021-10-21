package com.example.oasis.ui.workout

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.oasis.JsonReader
import com.example.oasis.State
import com.example.oasis.WorkoutType
import com.example.oasis.data.Repository
import com.example.oasis.model.Exercise
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application, private val workoutType: WorkoutType) : AndroidViewModel(application) {

    private val repository = Repository()

    private val _exercisesList = MutableLiveData<List<Exercise>>()
    val exercisesList: LiveData<List<Exercise>> = _exercisesList

    init {
        loadExercises(application)
    }

    fun updateBestResults(map: Map<String, Int>, workoutType: WorkoutType) {
        viewModelScope.launch {
            repository.updateBestResults(map, workoutType)
        }
    }

    private fun loadExercises(application: Application) {

        viewModelScope.launch {

            repository.getBestResults(workoutType).collect { state ->

                when (state) {

                    is State.Loading -> {

                        Log.d("firebase", "createExercises: loading")

                    }

                    is State.Success -> {

                        _exercisesList.apply {
                            value = JsonReader.getOrderedExercises(application, state.data, workoutType)
                        }

                    }

                    is State.Failed -> {

                        Log.d("firebase", "createExercises: failed")

                    }
                }
            }
        }
    }
}