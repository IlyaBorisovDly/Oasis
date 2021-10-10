package com.example.oasis.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.oasis.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding

    private lateinit var result: EditText
    private lateinit var decrease: TextView
    private lateinit var increase: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //toDo: Исправить
        binding = BottomSheetDialogBinding.inflate(layoutInflater)

        result = binding.resultEditText
        decrease = binding.decreaseTextView
        increase = binding.increaseTextView

        decrease.setOnClickListener {
            result.setText(result.text.toString().toInt())
        }

        increase.setOnClickListener {
            // val count = result.text.toString().toInt() - 1
            result.setText("11")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}