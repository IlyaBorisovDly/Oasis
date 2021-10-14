package com.example.oasis.ui.workout

import android.app.Application
import com.example.oasis.model.Exercise
import com.example.oasis.model.Workout
import org.json.JSONArray
import org.json.JSONObject

object WorkoutFactory {

    private lateinit var application: Application
    private lateinit var workoutType: WorkoutType

    fun createWorkout(application: Application, workoutType: WorkoutType): Workout {
        this.application = application
        this.workoutType = workoutType

        val exercises = loadExercisesList()

        return Workout(workoutType.title, exercises)
    }

    private fun loadExercisesList(): List<Exercise> {
        val exercisesList = mutableListOf<Exercise>()
        val bestResults = mutableMapOf<String, Int>()
        val jsonArray = loadJsonArray()

        for (i in 0 until jsonArray.length()) {
            val name = JSONObject(jsonArray[i].toString()).getString("name")
            val bestResult = bestResults[name] ?: 0

            exercisesList.add(Exercise(name, bestResult))
        }

        return exercisesList
    }

    private fun loadJsonArray(): JSONArray {
        val file = application.assets.open("workouts.json")
        val text = file.bufferedReader().use { it.readText() }
        val array = JSONArray(text)

        for (i in 0 until array.length()) {
            val jsonObject = JSONObject(array[i].toString())

            if (jsonObject.getString("workoutId") == workoutType.id.toString()) {
                return jsonObject.getJSONArray("exercises")
            }
        }

        return JSONObject(array[0].toString()).getJSONArray("exercises")
    }
}
