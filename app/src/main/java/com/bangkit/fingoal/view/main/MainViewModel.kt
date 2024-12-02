package com.bangkit.fingoal.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.fingoal.data.pref.UserModel
import com.bangkit.fingoal.data.repository.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getLoginState().asLiveData()
    }
}