package com.example.oasis.ui.workout

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.oasis.R
import com.example.oasis.databinding.BottomSheetDialogBinding
import com.example.oasis.databinding.InstanceExerciseCardBinding
import com.example.oasis.databinding.InstanceWorkoutButtonBinding
import com.example.oasis.model.Exercise
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class WorkoutAdapter(private val exercises: List<Exercise>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle("Закончить тренировку?")
            .setCancelable(true)
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Ок") { dialog, _ ->
                dialog.dismiss()
                saveResults()
                (context as Activity).finish()
            }
            .show()
    }

    // TODO: Написать отдельный класс для получения map из Room
    private fun saveResults() {
        exerciseHolders.forEach {
            it.getExercise()
        }
    }

    class ButtonHolder(
        binding: InstanceWorkoutButtonBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val button: Button = binding.button
    }

    class ExerciseHolder(
        private val binding: InstanceExerciseCardBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var exercise: Exercise

        private val exerciseBestResult: TextView = binding.bestResultTextView
        private val exerciseCount: TextView = binding.countTextView

        init {
            binding.exerciseCardView.setOnClickListener{ onCardClick() }
        }

        fun bind(exercise: Exercise) {
            this.exercise = exercise

            binding.exerciseNameTextView.text = exercise.name
            exerciseBestResult.text = exercise.bestResult.toString()
            exerciseCount.text = "0 / 4"
        }

        fun getExercise() = exercise

        private fun onCardClick() {
            val context = itemView.context
            val card = binding.exerciseCardView

            val dialogBinding = BottomSheetDialogBinding
                .inflate((context as AppCompatActivity).layoutInflater)

            val decrement = dialogBinding.decreaseTextView
            val increment = dialogBinding.increaseTextView
            val applyButton = dialogBinding.buttonApply

            val resultField = dialogBinding.resultEditText
            val warningMessage = dialogBinding.textViewWarning

            val dialog = BottomSheetDialog(context)

            dialog.setContentView(dialogBinding.bottomSheet)

            decrement.setOnClickListener { decreaseResult(resultField, warningMessage) }
            increment.setOnClickListener { increaseResult(resultField, warningMessage) }

            applyButton.setOnClickListener {
                if (isInputChecked(resultField, warningMessage)) {
                    applyResult(card, resultField)
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        private fun applyResult(card: MaterialCardView, resultField: EditText) {
            val result = resultField.text.toString().toInt()
            val previousBest = exercise.bestResult
            val nextCount = "${++exercise.count} / 4"

            if (result > previousBest) {
                val best = "$result кг"
                exercise.bestResult = result
                exerciseBestResult.text = best
            }

            exerciseCount.text = nextCount

            fillCardBackground(card)
        }

        private fun fillCardBackground(card: MaterialCardView) {
            val context = itemView.context

            when (exercise.count) {
                1 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_1)
                2 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_2)
                3 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_3)
                4 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_4)
            }
        }

        private fun increaseResult(resultField: EditText, warningMessage: TextView) {
            val text = resultField.text?.toString() ?: ""

            warningMessage.text = ""

            if (text.isNotBlank() && text.toInt() < 9999) {
                val number = text.toInt() + 1
                resultField.setText(number.toString())
            } else if (text.isBlank()) {
                warningMessage.text = "Введите число"
            } else if (text.toInt() >= 9999) {
                warningMessage.text = "Введено слишком большое число"
            }
        }

        private fun decreaseResult(resultField: EditText, warningMessage: TextView) {
            val text = resultField.text?.toString() ?: ""

            warningMessage.text = ""

            if (text.isNotBlank() && text.toInt() >= 1) {
                val number = text.toInt() - 1
                resultField.setText(number.toString())
            } else if (text.isBlank()) {
                warningMessage.text = "Введите число"
            } else if (text.toInt() <= 0) {
                warningMessage.text = "Число не может быть меньше нуля"
            }
        }

        private fun isInputChecked(resultField: EditText, warningMessage: TextView): Boolean {
            val text = resultField.text?.toString() ?: ""

            warningMessage.text = ""

            if (text.isNotBlank() && text.toInt() >= 0) {
                return true
            } else if (text.isBlank()) {
                warningMessage.text = "Введите число"
            } else if (text.toInt() < 0) {
                warningMessage.text = "Число не может быть меньше нуля"
            }

            return false
        }
    }
}
