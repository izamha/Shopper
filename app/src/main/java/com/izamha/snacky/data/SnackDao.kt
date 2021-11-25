package com.izamha.snacky.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.izamha.snacky.model.Snack

@Dao
interface SnackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createSnack(snack: Snack)

    @Update
    suspend fun updateSnack(snack: Snack)

    @Delete
    suspend fun deleteSnack(snack: Snack)

    // Queries

    @Query("SELECT * FROM snack_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Snack>>

    @Query("DELETE FROM snack_table")
    fun deleteAll()

}