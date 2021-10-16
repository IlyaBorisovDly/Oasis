package com.example.oasis.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oasis.data.Repository
import com.example.oasis.model.Exercise
import com.example.oasis.model.Workout

class WorkoutViewModel(application: Application, private val workout: Workout): AndroidViewModel(application) {

    private val _exercisesList = MutableLiveData<List<Exercise>>().apply { value = workout.exercises }
    var exercisesList: LiveData<List<Exercise>> = _exercisesList

    fun getAllBestResults() = Repository.getBestResults()

    fun getExercisesNames(): List<String> {
        val list = mutableListOf<String>()

        workout.exercises.forEach {
            list.add(it.name)
        }

        return list
    }
}