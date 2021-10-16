package com.example.oasis.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.data.Repository
import com.example.oasis.databinding.ActivityRegistrationBinding
import com.example.oasis.model.User
import com.example.oasis.ui.main.MainActivity
import com.example.oasis.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject

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

        nameField = binding.editTextRegisterName
        emailField = binding.editTextRegisterEmail
        passwordField = binding.editTextRegisterPassword
        registerButton = binding.buttonRegister

        initializeObservables()

        registerButton.setOnClickListener {
            val name = registrationViewModel.name.toString()
            val email = registrationViewModel.email.toString()
            val password = registrationViewModel.password.toString()

            register(auth, name, email, password)
        }
    }

    private fun register(auth: FirebaseAuth, name: String, email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val resultsMap = createResultsMap()
                val userId = auth.uid ?: "Error"
                val user = User(name, email, resultsMap)

                Repository.addUser(userId, user)

                startActivity(Intent(this, MainActivity::class.java))
            } else {
                showToast("Registration failed")
            }
        }
    }

    private fun createResultsMap(): MutableMap<String, Int> {
        val file = application.assets.open("workouts.json")
        val text = file.bufferedReader().use { it.readText() }
        val array = JSONArray(text)

        val resultsMap = mutableMapOf<String, Int>()

        for (i in 0 until array.length()) {
            val jsonObject = JSONObject(array[i].toString())
            val exercises  = jsonObject.getJSONArray("exercises")

            for (j in 0 until exercises.length()) {
                val innerObject = JSONObject(exercises[j].toString())
                val key = innerObject.get("name").toString()
                resultsMap[key] = 0
            }
        }

        return resultsMap
    }

    private fun initializeObservables() {
        registrationViewModel.name.observe(this, {
            nameField.setText(it)
        })

        registrationViewModel.email.observe(this, {
            emailField.setText(it)
        })

        registrationViewModel.password.observe(this, {
            passwordField.setText(it)
        })
    }
}