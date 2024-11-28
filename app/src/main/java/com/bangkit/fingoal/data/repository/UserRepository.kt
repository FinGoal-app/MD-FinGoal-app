package com.bangkit.fingoal.data.repository

import com.bangkit.fingoal.data.pref.UserModel
import com.bangkit.fingoal.data.pref.UserPreference
import com.bangkit.fingoal.data.response.LoginResponse
import com.bangkit.fingoal.data.response.RegisterResponse
import com.bangkit.fingoal.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiService: ApiService,
    private val pref: UserPreference
) {
    suspend fun postRegister(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun postLogin(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    fun getLoginState(): Flow<UserModel> {
        return pref.getSession()
    }

    fun getUsername(): Flow<String> {
        return pref.getUsername()
    }

    fun getToken(): Flow<String> {
        return pref.getLoginToken()
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}