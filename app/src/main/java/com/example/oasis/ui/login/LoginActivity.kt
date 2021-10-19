package com.example.oasis.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.databinding.ActivityLoginBinding
import com.example.oasis.ui.main.MainActivity
import com.example.oasis.ui.registration.RegistrationActivity
import com.example.oasis.ui.workout.WorkoutActivity
import com.example.oasis.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var auth: FirebaseAuth

    private lateinit var loginField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = Firebase.auth

        loginField = binding.editTextLoginEmail
        passwordField = binding.editTextLoginPassword
        loginButton = binding.buttonLogin
        registerTextView = binding.textViewRegister

        initializeObservables()

        loginButton.setOnClickListener {
            val login = loginField.text.toString()
            val password = passwordField.text.toString()

            signIn(auth, login, password)
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }

    private fun signIn(auth: FirebaseAuth, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showToast("SignIn failed")
            }
        }
    }

    private fun initializeObservables() {
        loginViewModel.login.observe(this, {
            loginField.setText(it)
        })

        loginViewModel.password.observe(this, {
            passwordField.setText(it)
        })
    }
}