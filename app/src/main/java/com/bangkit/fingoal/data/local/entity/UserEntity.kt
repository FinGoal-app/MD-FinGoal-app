package com.bangkit.fingoal.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey
    @ColumnInfo(name = "id_user")
    val idUser: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "balance")
    val balance: Double,
    @ColumnInfo(name = "savings")
    val savings: Double
): Parcelable