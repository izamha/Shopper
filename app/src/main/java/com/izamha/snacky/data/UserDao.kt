package com.izamha.snacky.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.izamha.snacky.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUser(email: String): LiveData<User>

}