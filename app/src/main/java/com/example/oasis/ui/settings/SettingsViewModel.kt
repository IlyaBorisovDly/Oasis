package com.example.oasis.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oasis.data.Repository
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private var _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    init {
        viewModelScope.launch {
            val list = Repository.getUserNameAndEmail()

            _name.apply { value = list[0] }
            _email.apply { value = list[1] }
        }
    }
}