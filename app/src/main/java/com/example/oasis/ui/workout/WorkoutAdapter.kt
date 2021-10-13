package com.example.oasis.ui.workout

import android.app.Activity
import android.content.Context
import android.util.Log
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

    // TODO: Написать отдельный класс для получения map из БД
    private fun saveResults() {
        val userId = Firebase.auth.currentUser?.uid ?: "Error"
        val currentUser = Firebase.database.getReference("Users").child(userId)
        val reference = currentUser.child("BestResults")

        val map = mutableMapOf<String, Int>()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val key = child.key.toString()
                    val value = child.value.toString().toInt()

                    map[key] = value
                }

                exerciseHolders.forEach {
                    val exercise = it.getExercise()
                    map[exercise.name] = exercise.bestResult
                }

                reference.setValue(map)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("firebase", "WorkoutFactory: onCancelled failure")
            }
        })
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
        private lateinit var exerciseCount: TextView

        private lateinit var dialogBinding: BottomSheetDialogBinding
        private lateinit var result: EditText
        private lateinit var warning: TextView

        init {
            binding.exerciseCardView.setOnClickListener{ onCardClick() }
        }

        fun bind(exercise: Exercise) {
            this.exercise = exercise


            exerciseCount = binding.countTextView

            binding.exerciseNameTextView.text = exercise.name
            exerciseBestResult.text = exercise.bestResult.toString()
            exerciseCount.text = "0 / 4"
        }

        fun getExercise() = exercise

        private fun onCardClick() {
            val context = itemView.context
            val card = binding.exerciseCardView
            val dialog = BottomSheetDialog(context)

            dialogBinding = BottomSheetDialogBinding
                .inflate((context as AppCompatActivity).layoutInflater)

            result = dialogBinding.resultEditText
            warning = dialogBinding.textViewWarning

            dialog.setContentView(dialogBinding.bottomSheet)

            dialogBinding.decreaseTextView.setOnClickListener { decreaseResult() }
            dialogBinding.increaseTextView.setOnClickListener { increaseResult() }

            dialogBinding.buttonApply.setOnClickListener {
                if (isInputChecked()) {
                    applyResult(card)
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        private fun applyResult(card: MaterialCardView) {
            val result = result.text.toString().toInt()
            val previousBest = exercise.bestResult
            val nextCount = "${++exercise.count} / 4"

            if (result > previousBest) {
                val best = "$result кг"
                exercise.bestResult = result
                exerciseBestResult.text = best
                // TODO: Прописать добавление в БД (после окончания тренировки)
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

        private fun increaseResult() {
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

        private fun decreaseResult() {
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

        private fun isInputChecked(): Boolean {
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

