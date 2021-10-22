package com.example.oasis.ui.workout.adapters

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.TransitionDrawable
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.oasis.ExerciseBottomSheetDialog
import com.example.oasis.R
import com.example.oasis.databinding.InstanceExerciseCardBinding
import com.example.oasis.databinding.InstanceWorkoutButtonBinding
import com.example.oasis.model.Exercise
import com.example.oasis.ui.workout.WorkoutActivity
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
class WorkoutAdapter(
    private val exercises: List<Exercise>,
    private val listener: WorkoutActivity.Listener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val exerciseHolders = mutableListOf<ExerciseHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding: ViewBinding

        return if (viewType == R.layout.instance_exercise_card) {
            itemBinding = InstanceExerciseCardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            ExerciseHolder(itemBinding)
        } else {
            itemBinding = InstanceWorkoutButtonBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ButtonHolder(itemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExerciseHolder) {
            val itemExercise = exercises[position]

            holder.bind(itemExercise)
            exerciseHolders.add(holder)
        } else if (holder is ButtonHolder) {
            holder.button.setOnClickListener { showAlertDialog(holder.itemView.context) }
        }
    }

    override fun getItemCount(): Int {
        return exercises.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < exercises.size) {
            R.layout.instance_exercise_card
        } else {
            R.layout.instance_workout_button
        }
    }

    private fun showAlertDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder
            .setTitle("Закончить тренировку?")
            .setCancelable(true)
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Ок") { dialog, _ ->
                uploadBestResults()
                dialog.dismiss()
                (context as Activity).finish()
            }

        dialogBuilder.show()
    }

    private fun uploadBestResults() {
        val map = mutableMapOf<String, Int>()

        exerciseHolders.forEach { holder ->
            val exercise = holder.getExercise()
            map[exercise.name] = exercise.bestResult
        }

        listener.onResultsApplied(map)
    }

    class ButtonHolder(
        binding: InstanceWorkoutButtonBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val button: Button = binding.button
    }

    class ExerciseHolder(binding: InstanceExerciseCardBinding): RecyclerView.ViewHolder(binding.root) {

        private lateinit var exercise: Exercise

        private val card = binding.exerciseCardView
        private val exerciseName: TextView = binding.exerciseNameTextView
        private val exerciseBestResult: TextView = binding.bestResultTextView
        private val exerciseCount: TextView = binding.countTextView

        fun bind(exercise: Exercise) {
            this.exercise = exercise

            val bestResult = "${exercise.bestResult} кг."
            val count = "${exercise.count} / 4"

            exerciseName.text = exercise.name
            exerciseBestResult.text = bestResult
            exerciseCount.text = count

            card.setOnClickListener{ onCardClick() }
        }

        fun getExercise() = exercise

        private fun onCardClick() {
            val context = itemView.context
            val dialog = ExerciseBottomSheetDialog(context)

            dialog.applyButton.setOnClickListener {
                val input: String? = dialog.getInput()

                if (input != null) {
                    applyResult(input.toInt())
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        private fun applyResult(result: Int) {
            val previousBest = exercise.bestResult
            val nextCount = "${++exercise.count} / 4"

            if (result > previousBest) {
                val best = "$result кг"
                exercise.bestResult = result
                exerciseBestResult.text = best
            }

            exerciseCount.text = nextCount
        }
    }
}

