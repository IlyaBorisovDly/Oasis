package com.example.oasis.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.databinding.ActivityRegistrationBinding
import com.example.oasis.ui.workout.WorkoutActivity
import com.example.oasis.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationViewModel: RegistrationViewModel

    private lateinit var auth: FirebaseAuth

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        auth = Firebase.auth

        nameField = binding.nameEditText
        emailField = binding.loginEditText
        passwordField = binding.passwordEditText
        registerButton = binding.registerButton

        initializeObservables()

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            register(auth, name, email, password)
        }
    }

    private fun register(auth: FirebaseAuth, name: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, WorkoutActivity::class.java))
            } else {
                showToast("Registration failed")
            }
        }
    }

    private fun initializeObservables() {
        registrationViewModel.name.observe(this, {
            nameField.setText(it)
        })

        registrationViewModel.login.observe(this, {
            emailField.setText(it)
        })

        registrationViewModel.password.observe(this, {
            passwordField.setText(it)
        })
    }
}