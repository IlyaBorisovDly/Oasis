package com.example.oasis.ui.workout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.R
import com.example.oasis.databinding.BottomSheetDialogBinding
import com.example.oasis.model.Exercise
import com.example.oasis.databinding.InstanceExerciseCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView

class WorkoutAdapter(private val exercises: List<Exercise>): RecyclerView.Adapter<WorkoutAdapter.ExerciseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val itemBinding = InstanceExerciseCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ExerciseHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        val itemExercise = exercises[position]
        holder.bind(itemExercise)
    }

    override fun getItemCount(): Int = exercises.size

    class ExerciseHolder(
        private val binding: InstanceExerciseCardBinding
        ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var exercise: Exercise

        private lateinit var exerciseName: TextView
        private lateinit var exerciseBestResult: TextView
        private lateinit var exerciseCount: TextView

        init {
            binding.exerciseCardView.setOnClickListener(this)
        }

        fun bind(exercise: Exercise) {
            this.exercise = exercise

            exerciseName = binding.exerciseNameTextView
            exerciseBestResult = binding.bestResultTextView
            exerciseCount = binding.countTextView

            exerciseName.text = exercise.name
            exerciseBestResult.text = exercise.bestResult.toString()
            exerciseCount.text = "0 / 4"
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            val card = binding.exerciseCardView

            val bind = BottomSheetDialogBinding.inflate((context as AppCompatActivity).layoutInflater)

            val result = bind.resultEditText
            val warning = bind.textViewWarning

            val dialog = BottomSheetDialog(context)

            dialog.setContentView(bind.bottomSheet)

            bind.decreaseTextView.setOnClickListener { decrease(result, warning) }
            bind.increaseTextView.setOnClickListener { increase(result, warning) }

            bind.buttonApply.setOnClickListener {
                if (isInputChecked(result, warning)) {
                    dialog.dismiss()
                    fillCardBackground(context, card)
                    val newStr = "${exercise.count} / 4"
                    exerciseCount.text = newStr
                    exerciseBestResult.text = "${result.text} кг"
                }
            }

            dialog.show()
        }

        private fun fillCardBackground(context: Context, card: MaterialCardView) {
            when (exercise.count) {
                0 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_1)
                1 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_2)
                2 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_3)
                3 -> card.background = AppCompatResources.getDrawable(context, R.drawable.fill_card_4)
            }

            exercise.count++
        }

        private fun increase(result: EditText, warning: TextView) {
            val text = result.text?.toString() ?: ""

            warning.text = ""

            if (text.isNotBlank() && text.toInt() < 9999) {
                val number = text.toInt() + 1
                result.setText(number.toString())
            } else if (text.isBlank()) {
                warning.text = "Введите число"
            } else if (text.toInt() >= 9999) {
                warning.text = "Введено слишком большое число"
            }
        }

        private fun decrease(result: EditText, warning: TextView) {
            val text = result.text?.toString() ?: ""

            warning.text = ""

            if (text.isNotBlank() && text.toInt() >= 1) {
                val number = text.toInt() - 1
                result.setText(number.toString())
            } else if (text.isBlank()) {
                warning.text = "Введите число"
            } else if (text.toInt() <= 0) {
                warning.text = "Число не может быть меньше нуля"
            }
        }

        private fun isInputChecked(result: EditText, warning: TextView): Boolean {
            val text = result.text?.toString() ?: ""

            warning.text = ""

            if (text.isNotBlank() && text.toInt() >= 0) {
                return true
            } else if (text.isBlank()) {
                warning.text = "Введите число"
            } else if (text.toInt() < 0) {
                warning.text = "Число не может быть меньше нуля"
            }

            return false
        }
    }
}