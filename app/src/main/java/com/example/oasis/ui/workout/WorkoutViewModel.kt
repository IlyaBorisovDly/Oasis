package com.example.oasis.ui.workout

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.oasis.State
import com.example.oasis.WorkoutType
import com.example.oasis.data.Repository
import com.example.oasis.model.Exercise
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

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
                    }

                    is State.Success -> {
                        val exercises = getOrderedExercises(application, state.data)
                        _exercisesList.apply { value = exercises }
                    }

                    is State.Failed -> {
                        Log.d("firebase", "createExercises: failed")
                    }
                }
            }
        }
    }

    private fun getOrderedExercises(application: Application, map: Map<String, Int>): List<Exercise> {
        val orderedExercises = getOrderSet(application)
        val list = mutableListOf<Exercise>()

        orderedExercises.forEach {
            val bestResult = map.getValue(it)
            list.add(Exercise(it, bestResult))
        }

        return list
    }

    private fun getOrderSet(application: Application): LinkedHashSet<String> {
        val set = linkedSetOf<String>()
        val file = application.assets.open("workouts.json")
        val text = file.bufferedReader().use { it.readText() }
        val array = JSONArray(text)

        for (i in 0 until array.length()) {
            val jsonObject = JSONObject(array[i].toString())

            if (jsonObject.getInt("workoutId") == workoutType.id) {
                val exercises  = jsonObject.getJSONArray("exercises")

                for (j in 0 until exercises.length()) {
                    val innerObject = JSONObject(exercises[j].toString())
                    val name = innerObject.get("name").toString()
                    set.add(name)

                }
            }
        }

        return set
    }
}