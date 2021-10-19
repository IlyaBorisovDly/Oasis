package com.example.oasis.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.databinding.ActivitySettingsBinding
import com.example.oasis.ui.login.LoginActivity
import com.example.oasis.ui.main.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var nameField: TextView
    private lateinit var emailField: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameField = binding.textViewNameOutput
        emailField = binding.textViewEmailOutput
        signOutButton = binding.signOutbutton

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        initializeObservers()

        signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun initializeObservers() {
        settingsViewModel.name.observe(this, {
            nameField.text = it
        })

        settingsViewModel.email.observe(this, {
            emailField.text = it
        })
    }
}