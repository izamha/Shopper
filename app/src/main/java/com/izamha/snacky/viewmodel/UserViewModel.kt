package com.izamha.snacky.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.izamha.snacky.data.SnackDatabase
import com.izamha.snacky.data.UserDatabase
import com.izamha.snacky.model.Snack
import com.izamha.snacky.model.User
import com.izamha.snacky.repository.SnackRepo
import com.izamha.snacky.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repo: UserRepo

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        repo = UserRepo(userDao)
    }

    fun createUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createUser(user = user)
        }
    }

    fun getUser(email: String): LiveData<User> {
       return repo.getUser(email = email)
    }
}

class UserViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}