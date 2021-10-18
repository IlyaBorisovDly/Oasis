package com.example.oasis.ui.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.State
import com.example.oasis.databinding.ActivityWorkoutBinding
import com.example.oasis.model.Exercise
import com.example.oasis.utils.showToast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.json.JSONArray
import org.json.JSONObject

@InternalCoroutinesApi
class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var workoutType: WorkoutType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        workoutType =
            if (bundle?.get("WorkoutType") is WorkoutType) {
                bundle.get("WorkoutType") as WorkoutType
            } else {
                WorkoutType.FIRST
            }

        workoutViewModel =
           ViewModelProvider(this, WorkoutViewModelFactory(application, workoutType))
               .get(WorkoutViewModel::class.java)


        CoroutineScope(Dispatchers.Main).launch {
            initializeRecyclerView()
        }
    }

    private suspend fun initializeRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        workoutViewModel.getBestResults(workoutType).collect { state ->
            when (state) {

                is State.Loading -> {
                    recyclerView.adapter = WorkoutAdapterEmpty()
                    showToast("Loading...")
                }

                is State.Success -> {
                    val exercisesList = getOrderedExercises(state.data)
                    val listener = getListener()

                    workoutViewModel.exercisesList.value = exercisesList.value
                    recyclerView.adapter = WorkoutAdapter(workoutViewModel.exercisesList, listener)
                }

                is State.Failed -> {
                    showToast("Failed...")
                }
            }
        }
    }

    private fun getListener(): Listener {
        return object : Listener {

            override fun onExerciseChanged() {
                TODO("Not yet implemented")
            }

            override fun onResultsApplied(map: Map<String, Int>) {
                workoutViewModel.updateBestResults(map, workoutType)
            }

        }
    }

    private fun getOrderedExercises(map: Map<String, Int>): MutableLiveData<List<Exercise>> {
        val exercisesOrderSet = getOrderSet()
        val list = mutableListOf<Exercise>()

        exercisesOrderSet.forEach {
            val bestResult = map.getValue(it)
            list.add(Exercise(it, bestResult))
        }

        return MutableLiveData<List<Exercise>>().apply { value = list }
    }

    private fun getOrderSet(): LinkedHashSet<String> {
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

    interface Listener {
        fun onExerciseChanged()

        fun onResultsApplied(map: Map<String, Int>)
    }
}