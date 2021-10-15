package com.example.oasis.ui.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RegistrationViewModel(application: Application): AndroidViewModel(application) {

    private val _name = MutableLiveData<String>().apply { value = "" }
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>().apply { value = "" }
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>().apply { value = "" }
    val password: LiveData<String> = _password

//    private val repository: UserRepository

//    init {
//        val userDao = UserDatabase.getDatabase(application).userDao()
//        repository = UserRepository(userDao)
//        // readBestResults = repository.readBestResults
//    }

//    fun addUser(user: RoomUser) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.addUser(user)
//            Log.d("firebase", "addUser: successful!!!")
//        }
//    }
}