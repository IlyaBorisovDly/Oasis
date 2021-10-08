package com.example.oasis.data.repository

import androidx.lifecycle.LiveData
import com.example.oasis.model.User
import com.example.oasis.data.local.UserDAO

class UserRepository(private val userDao: UserDAO) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}