package com.bangkit.fingoal.view.activities.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> = _balance
    
    private val _savings = MutableLiveData<Double>()
    val savings: LiveData<Double> = _savings

    private val _allocation = MutableLiveData<Double>()
    val allocation: LiveData<Double> = _allocation

    fun getUserProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getHome()
                _name.value = response.showHome?.nama.toString()
                _balance.value = response.showHome?.balance?.toDouble() ?: 0.0
                _savings.value = response.showHome?.savings?.toDouble() ?: 0.0
                _allocation.value = response.showHome?.amountAllocation?.toDouble() ?: 0.0
                _isLoading.value = false
                Log.d("ProfileViewModel", "Profile: $response")
            } catch (e: Exception) {
                _isLoading.value = false
                _name.value = ""
                _balance.value = 0.0
                _savings.value = 0.0
                _allocation.value = 0.0
                Log.e("ProfileViewModel", "Profile: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val response = userRepository.logout()
                Log.d("ProfileViewModel", "Logout: $response")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Logout: ${e.message}")
            }
        }
    }
}