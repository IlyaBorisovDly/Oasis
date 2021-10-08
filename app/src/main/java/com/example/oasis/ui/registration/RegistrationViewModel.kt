package com.example.oasis.ui.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

    private val _name = MutableLiveData<String>().apply { value = "" }
    val name: LiveData<String> = _name

    private val _login = MutableLiveData<String>().apply { value = "" }
    val login: LiveData<String> = _login

    private val _password = MutableLiveData<String>().apply { value = "" }
    val password: LiveData<String> = _password


}