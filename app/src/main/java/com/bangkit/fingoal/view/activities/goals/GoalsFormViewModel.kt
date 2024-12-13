package com.bangkit.fingoal.view.activities.goals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.retrofit.AddGoalRequest
import kotlinx.coroutines.launch

class GoalsFormViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _goalResult = MutableLiveData<String?>()
    val goalResult: LiveData<String?> = _goalResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun goal (addGoalRequest: AddGoalRequest) {
        _isLoading.value = true
        _goalResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postGoal(addGoalRequest)
                _goalResult.value = response.message
                _isSuccess.value = true
                _isLoading.value = false
                Log.d("Goal", response.message.toString())
            } catch (e: Exception) {
                _isSuccess.value = false
                _isLoading.value = false
                _goalResult.value = e.message
                Log.d("Goal", e.message.toString())
            }
        }
    }
}