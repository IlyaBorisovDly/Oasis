package com.example.oasis

import android.app.Application
import com.example.oasis.model.Exercise
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.LinkedHashSet

object JsonReader {

    fun getResultsMap(application: Application, workoutType: WorkoutType): Map<String, Int> {
        val resultsMap = mutableMapOf<String, Int>()

        val file = if (Locale.getDefault().country == "RU") {
            application.assets.open("workouts.json")
        } else {
            application.assets.open("workouts_en.json")
        }

        val text = file.bufferedReader().use { it.readText() }
        val array = JSONArray(text)

        for (i in 0 until array.length()) {
            val jsonObject = JSONObject(array[i].toString())

            if (jsonObject.getInt("workoutId") == workoutType.id) {
                val exercises  = jsonObject.getJSONArray("exercises")

                for (j in 0 until exercises.length()) {
                    val innerObject = JSONObject(exercises[j].toString())
                    val key = innerObject.get("name").toString()
                    resultsMap[key] = 0
                }
            }
        }

        return resultsMap
    }

    fun getOrderedExercises(application: Application, map: Map<String, Int>, workoutType: WorkoutType): List<Exercise> {
        val orderedExercises = getOrderSet(application, workoutType)
        val list = mutableListOf<Exercise>()

        orderedExercises.forEach {
            val bestResult = map.getValue(it)
            list.add(Exercise(it, bestResult))
        }

        return list
    }

    private fun getOrderSet(application: Application, workoutType: WorkoutType): LinkedHashSet<String> {
        val set = linkedSetOf<String>()

        val file = if (Locale.getDefault().country == "RU") {
            application.assets.open("workouts.json")
        } else {
            application.assets.open("workouts_en.json")
        }

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