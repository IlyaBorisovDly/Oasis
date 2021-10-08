package com.example.oasis.ui.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oasis.model.User
import com.example.oasis.databinding.ActivityRegistrationBinding
import com.example.oasis.ui.UserViewModel
import com.example.oasis.utils.showToast

class RegistrationActivity : AppCompatActivity() {
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameEditText = binding.nameEditText
        val loginEditText = binding.loginEditText
        val passwordEditText = binding.passwordEditText
        val registerButton = binding.registerButton

        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        registrationViewModel.name.observe(this, Observer {
            nameEditText.setText(it)
        })

        registrationViewModel.login.observe(this, Observer {
            loginEditText.setText(it)
        })

        registrationViewModel.password.observe(this, Observer {
            passwordEditText.setText(it)
        })

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            register(name, login, password)
        }
    }

    private fun register(name: String, login: String, password: String) {
        if (!checkData(name, login, password)) {
            Toast.makeText(this,"Fuck u dumass", Toast.LENGTH_SHORT).show()
        } else {
            val user = User(0, name, login, password)
            userViewModel.addUser(user)
            showToast("Success")
        }
    }

    private fun checkData(name: String, login: String, password: String): Boolean {
        return (name.isNotBlank() && login.isNotBlank() && password.isNotBlank())
    }
}