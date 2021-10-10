package com.example.oasis.ui.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.R
import com.example.oasis.model.Exercise
import com.example.oasis.databinding.InstanceExerciseCardBinding
import com.example.oasis.ui.BottomSheetFragment

class ExerciseAdapter(private val exercises: ArrayList<Exercise>): RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder>() {

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

    class ExerciseHolder(private val binding: InstanceExerciseCardBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var exercise: Exercise? = null

        init {
            binding.exerciseCardView.setOnClickListener(this)
        }

        fun bind(exercise: Exercise) {
            val count = "${exercise.count} / 4"

            this.exercise = exercise

            binding.exerciseNameTextView.text = exercise.name
            binding.bestResultTextView.text = exercise.bestResult.toString()
            binding.countTextView.text = count
        }

        override fun onClick(v: View?) {
            val context = itemView.context

            val card = binding.exerciseCardView

            val sManager = (context as AppCompatActivity).supportFragmentManager

            BottomSheetFragment().apply {
                show(sManager, "Tag")
            }

            when (exercise!!.count) {
                0 -> {
                    card.background = AppCompatResources.getDrawable(context,
                        R.drawable.fill_card_1
                    )
                    exercise!!.count++
                }
                1 -> {
                    card.background = AppCompatResources.getDrawable(context,
                        R.drawable.fill_card_2
                    )
                    exercise!!.count++
                }
                2 -> {
                    card.background = AppCompatResources.getDrawable(context,
                        R.drawable.fill_card_3
                    )
                    exercise!!.count++
                }
                3 -> {
                    card.background = AppCompatResources.getDrawable(context,
                        R.drawable.fill_card_4
                    )
                    exercise!!.count++
                }
                else -> exercise!!.count++
            }

        }
    }


}