package com.izamha.snacky.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

) : Parcelable {
    constructor(
        id: Long,
        username: String,
        email: String,
        phoneNumber: String,
        password: String
    ): this(id) {
        this.id = id
        this.username = username
        this.email = email
        this.phoneNumber = phoneNumber
        this.password = password
    }
}