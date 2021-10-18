package com.example.oasis.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oasis.databinding.InstanceExerciseCardEmptyBinding

class WorkoutAdapterEmpty: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val itemBinding = InstanceExerciseCardEmptyBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return CardHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 7
    }


    class CardHolder(
        binding: InstanceExerciseCardEmptyBinding
    ): RecyclerView.ViewHolder(binding.root)
}