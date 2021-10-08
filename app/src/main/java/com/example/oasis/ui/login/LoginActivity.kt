package com.example.oasis.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oasis.R
import com.example.oasis.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
    }
}