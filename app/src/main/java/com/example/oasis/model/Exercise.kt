package com.example.oasis.model

import com.example.oasis.R

data class Exercise(
    val name: String,
    var bestResult: Int,
    var count: Int = 0,
)