package com.example.oasis.model

data class User(
    var name: String,
    var login: String,
    var password: String,
    var bestResults: Map<String, Double> = mutableMapOf()
)