package com.bangkit.fingoal.data.di

import android.content.Context
import com.bangkit.fingoal.data.pref.UserPreference
import com.bangkit.fingoal.data.pref.dataStore
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.retrofit.ApiConfig
import com.bangkit.fingoal.data.retrofit.ApiConfigML
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val apiServiceML = ApiConfigML.getApiServiceML()
        return UserRepository.getInstance(apiServiceML, apiService, pref)
    }
}