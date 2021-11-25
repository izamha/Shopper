package com.izamha.snacky.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.izamha.snacky.data.SnackDatabase
import com.izamha.snacky.model.Snack
import com.izamha.snacky.model.SnackbarManager
import com.izamha.snacky.model.snacks
import com.izamha.snacky.repository.SnackRepo
import com.izamha.snacky.ui.home.cart.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SnackViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Snack>>
    private val repo: SnackRepo

    init {
        val snackDao = SnackDatabase.getInstance(application).snackDao()
        repo = SnackRepo(snackDao)
        readAllData = repo.readAllData
    }

    fun createSnack(snack: Snack) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createSnack(snack)
        }
    }

    fun updateSnack(snack: Snack) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateSnack(snack)
        }
    }

    fun deleteSnack(snack: Snack) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteSnack(snack = snack)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }

}

class SnackViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(SnackViewModel::class.java)) {
            return SnackViewModel(application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}
