package com.izamha.snacky.repository

import androidx.lifecycle.LiveData
import com.izamha.snacky.data.SnackDao
import com.izamha.snacky.model.Snack

class SnackRepo(private val snackDao: SnackDao) {

    val readAllData: LiveData<List<Snack>> = snackDao.readAllData()

    // Create
    suspend fun createSnack(snack: Snack) {
        snackDao.createSnack(snack)
    }

    suspend fun updateSnack(snack: Snack) {
        snackDao.updateSnack(snack)
    }

    suspend fun deleteSnack(snack: Snack) {
        snackDao.deleteSnack(snack)
    }

    suspend fun deleteAll() {
        snackDao.deleteAll()
    }

}