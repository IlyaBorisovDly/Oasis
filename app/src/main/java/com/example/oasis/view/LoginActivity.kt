package com.example.oasis.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oasis.R
import com.example.oasis.databinding.ActivityLoginBinding
import com.example.oasis.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
    }
}