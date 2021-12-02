package com.izamha.snacky.repository

import androidx.lifecycle.LiveData
import com.izamha.snacky.data.UserDao
import com.izamha.snacky.model.User

class UserRepo(private val userDao: UserDao) {

    // Create
    suspend fun createUser(user: User) {
        userDao.createUser(user)
    }

    // Get User
    fun getUser(email: String): LiveData<User> {
        return userDao.getUser(email = email)
    }

}