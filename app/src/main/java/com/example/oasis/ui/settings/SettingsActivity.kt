package com.example.oasis.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.oasis.databinding.ActivitySettingsBinding
import com.example.oasis.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth
        val users = Firebase.database.getReference("Users")
        val userId = auth.currentUser?.uid ?: ""

        userName = binding.textViewUsername
        userEmail = binding.textViewUserEmail
        signOutButton = binding.signOutbutton

        users.child(userId).child("Name").get().addOnSuccessListener {
            val text = "Здравствуйте, ${it.value.toString()}"
            userName.text = text
        }

        users.child(userId).child("Email").get().addOnSuccessListener {
            userEmail.text = it.value.toString()
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}