package com.example.oasis.ui.workout

import android.app.Application
import android.util.Log
import com.example.oasis.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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

            return getExercisesList()
        }

        private fun getExercisesList(): List<Exercise> {
            val exercisesList = mutableListOf<Exercise>()

            val userId = Firebase.auth.currentUser?.uid ?: "Error"
            val currentUser = Firebase.database.getReference("Users").child(userId)

            val jsonArray = getJsonArray()

            val bestResults = mutableMapOf<String, Double>()
            val reference = currentUser.child("BestResults")

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val key = child.key.toString()
                        val value = child.value.toString().toDouble()
                        bestResults[key] = value
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("firebase", "WorkoutFactory: onCancelled failure")
                }
            })

            for (i in 0 until jsonArray.length()) {
                val name = JSONObject(jsonArray[i].toString()).getString("name")
                val bestResult = bestResults[name] ?: 0.0

                exercisesList.add(Exercise(name, bestResult))
            }

            return exercisesList
        }

        private fun getJsonArray(): JSONArray {
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