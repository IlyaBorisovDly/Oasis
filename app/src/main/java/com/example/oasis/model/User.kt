package com.example.oasis.model

data class User(
    var name: String,
    var email: String,
    var bestResults1: Map<String, Int>,
    var bestResults2: Map<String, Int>,
    var bestResults3: Map<String, Int>,
)


