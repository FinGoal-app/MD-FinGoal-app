package com.bangkit.fingoal.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.fingoal.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id_user = :userId")
    fun getUserById(userId: Int): Flow<UserEntity>

    @Query("UPDATE users SET balance = :newBalance WHERE id_user = :userId")
    suspend fun updateBalance(userId: Int, newBalance: Double)

    @Query("UPDATE users SET savings = :newSavings WHERE id_user = :userId")
    suspend fun updateSavings(userId: Int, newSavings: Double)
}