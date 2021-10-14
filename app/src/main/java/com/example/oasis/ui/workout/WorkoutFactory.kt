package com.example.oasis.ui.workout

import android.app.Application
import android.util.Log
import com.example.oasis.model.Exercise
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject

class WorkoutFactory {

    companion object {

        private lateinit var application: Application
        private lateinit var workout: Workout

        fun createWorkout(application: Application, workout: Workout): List<Exercise> {
            this.application = application
            this.workout = workout

            return loadExercisesList()
        }

        private fun loadExercisesList(): List<Exercise> {
            val exercisesList = mutableListOf<Exercise>()

            val userId = Firebase.auth.currentUser?.uid ?: "Error"
            // val currentUser = Firebase.database.getReference("users").child(userId)


            val jsonArray = loadJsonArray()

            val bestResults = mutableMapOf<String, Int>()

            Log.d("firebase", "loadExercisesList: $bestResults")

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

                if (jsonObject.getString("workoutId") == workout.number.toString()) {
                    return jsonObject.getJSONArray("exercises")
                }
            }

            return JSONObject(array[0].toString()).getJSONArray("exercises")
        }
    }



}