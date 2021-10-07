package com.example.oasis.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _login = MutableLiveData<String>().apply { value = "" }
    val login: LiveData<String> = _login
}