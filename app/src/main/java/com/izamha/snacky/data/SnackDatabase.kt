package com.izamha.snacky.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.izamha.snacky.model.Snack

@Database(entities = [Snack::class], version = 1, exportSchema = false)
abstract class SnackDatabase : RoomDatabase() {

    abstract fun snackDao(): SnackDao

    companion object {
        @Volatile
        private var INSTANCE: SnackDatabase? = null

        fun getInstance(context: Context): SnackDatabase {
            synchronized(this) {
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    SnackDatabase::class.java,
                    "snack_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

}