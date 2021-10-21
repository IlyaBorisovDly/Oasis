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

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initializeObservables()

        binding.buttonLogin.setOnClickListener {
            val login = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            signIn(login, password)
        }

        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        val auth = Firebase.auth

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
            binding.editTextLoginEmail.setText(it)
        })

        loginViewModel.password.observe(this, {
            binding.editTextLoginPassword.setText(it)
        })
    }
}