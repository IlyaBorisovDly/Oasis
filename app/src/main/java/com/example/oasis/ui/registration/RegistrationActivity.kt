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
import com.example.oasis.WorkoutType
import com.example.oasis.ui.login.LoginActivity
import com.example.oasis.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.InternalCoroutinesApi
import org.json.JSONArray
import org.json.JSONObject

@InternalCoroutinesApi
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
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            register(auth, name, email, password)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
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

    private fun register(auth: FirebaseAuth, name: String, email: String, password: String){

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
        val map1 = createResultsMap(WorkoutType.FIRST)
        val map2 = createResultsMap(WorkoutType.SECOND)
        val map3 = createResultsMap(WorkoutType.THIRD)

        return User(name, email, map1, map2, map3)
    }

    private fun createResultsMap(workoutType: WorkoutType): Map<String, Int> {
        val resultsMap = mutableMapOf<String, Int>()

        val file = application.assets.open("workouts.json")
        val text = file.bufferedReader().use { it.readText() }
        val array = JSONArray(text)

        for (i in 0 until array.length()) {
            val jsonObject = JSONObject(array[i].toString())

            if (jsonObject.getInt("workoutId") == workoutType.id) {
                val exercises  = jsonObject.getJSONArray("exercises")

                for (j in 0 until exercises.length()) {
                    val innerObject = JSONObject(exercises[j].toString())
                    val key = innerObject.get("name").toString()
                    resultsMap[key] = 0
                }
            }
        }

        return resultsMap
    }

}