package com.bangkit.fingoal.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ResultHistoryItem
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private val _homeResult = MutableLiveData<List<ResultHistoryItem>>()
    val homeResult: LiveData<List<ResultHistoryItem>> = _homeResult
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> = _balance
    private val _savings = MutableLiveData<Double>()
    val savings: LiveData<Double> = _savings

    fun getHome() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getHome()
                _isSuccess.value = true
                _homeResult.value = response.showHome?.resultHistory
                _balance.value = response.showHome?.balance?.toDouble() ?: 0.0
                _savings.value = response.showHome?.savings?.toDouble() ?: 0.0
                _isLoading.value = false
                Log.d("HomeViewModel", "Home: $response")
                Log.d("HomeViewModel", "Home: ${response.message}")
            } catch (e: Exception) {
                _isLoading.value = false
                _isSuccess.value = false
                _homeResult.value = emptyList()
                _balance.value = 0.0
                _savings.value = 0.0
                Log.e("HomeViewModel", "Home: ${e.message}")
            }
        }
    }
}