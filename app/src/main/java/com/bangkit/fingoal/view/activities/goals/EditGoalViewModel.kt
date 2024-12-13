package com.bangkit.fingoal.view.activities.goals

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.response.ShowGoalItem
import com.bangkit.fingoal.data.retrofit.UpdateGoalRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EditGoalViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _goal = MutableLiveData<ShowGoalItem?>()
    val goal: MutableLiveData<ShowGoalItem?> = _goal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _updateGoalResult = MutableLiveData<String?>()
    val updateGoalResult: MutableLiveData<String?> = _updateGoalResult

    fun updateGoal(id: String, updateGoalRequest: UpdateGoalRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.updateGoal(id, updateGoalRequest)
                _updateGoalResult.value = response.message
                _isLoading.value = false
                Log.d("EditGoalViewModel", "EditGoal: ${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _updateGoalResult.value = errorResponse.message
                _isLoading.value = false
                Log.e("EditGoalViewModel", "EditGoal: ${errorResponse.message}")
            }
        }
    }
}