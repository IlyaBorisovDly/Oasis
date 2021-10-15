package com.example.oasis.ui.workout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.oasis.model.Exercise
import com.example.oasis.model.Workout

class WorkoutViewModel(application: Application, workout: Workout): AndroidViewModel(application) {

    private val _exercisesList = MutableLiveData<List<Exercise>>().apply { value = workout.exercises }
    var exercisesList: LiveData<List<Exercise>> = _exercisesList

    private val _workoutName = MutableLiveData<String>().apply { value = workout.name }
    var workoutName: LiveData<String> = _workoutName
}