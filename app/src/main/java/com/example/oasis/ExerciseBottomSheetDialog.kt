package com.example.oasis

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.oasis.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ExerciseBottomSheetDialog(context: Context): BottomSheetDialog(context) {

    private val dialogBinding = BottomSheetDialogBinding
        .inflate((context as AppCompatActivity).layoutInflater)

    val applyButton = dialogBinding.buttonApply

    private val resultField = dialogBinding.editTextResult
    private val warningMessage = dialogBinding.textViewWarning

    init {
        setContentView(dialogBinding.bottomSheet)

        dialogBinding.textViewDecrease.setOnClickListener { decreaseResult() }
        dialogBinding.textViewIncrease.setOnClickListener { increaseResult() }
    }

    fun getInput() = if (isInputChecked()) resultField.text.toString() else null

    private fun decreaseResult() {
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

    private fun increaseResult() {
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

    private fun isInputChecked(): Boolean {
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