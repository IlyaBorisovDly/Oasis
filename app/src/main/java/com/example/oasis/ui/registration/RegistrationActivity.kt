package com.example.oasis.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.JsonReader
import com.example.oasis.data.Repository
import com.example.oasis.databinding.ActivityRegistrationBinding
import com.example.oasis.model.User
import com.example.oasis.ui.main.MainActivity
import com.example.oasis.WorkoutType
import com.example.oasis.ui.login.LoginActivity
import com.example.oasis.utils.showToast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationViewModel: RegistrationViewModel

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        initializeObservables()

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextRegisterName.text.toString()
            val email = binding.editTextRegisterEmail.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()

            register(name, email, password)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun initializeObservables() {
        registrationViewModel.name.observe(this, {
            binding.editTextRegisterName.setText(it)
        })

        registrationViewModel.email.observe(this, {
            binding.editTextRegisterEmail.setText(it)
        })

        registrationViewModel.password.observe(this, {
            binding.editTextRegisterPassword.setText(it)
        })
    }

    private fun register(name: String, email: String, password: String){
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val userId = auth.uid ?: "Error"
                val user = createUser(name, email)

                val repository = Repository()
                repository.addUser(userId, user)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showToast("Registration failed")
            }
        }
    }

    private fun createUser(name: String, email: String): User {

        val map1 = JsonReader.getResultsMap(application, WorkoutType.FIRST)
        val map2 = JsonReader.getResultsMap(application, WorkoutType.SECOND)
        val map3 = JsonReader.getResultsMap(application, WorkoutType.THIRD)

        return User(name, email, map1, map2, map3)
    }
}