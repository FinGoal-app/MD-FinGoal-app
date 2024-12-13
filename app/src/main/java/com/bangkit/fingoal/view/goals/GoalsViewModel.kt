package com.bangkit.fingoal.view.goals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.response.ShowGoalItem
import com.bangkit.fingoal.data.retrofit.PredictRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GoalsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _goals = MutableLiveData<List<ShowGoalItem>?>()
    val goals: LiveData<List<ShowGoalItem>?> = _goals

    private val _deleteResult = MutableLiveData<String?>()
    val deleteResult: LiveData<String?> = _deleteResult

    private val _predictResult = MutableLiveData<String?>()
    val predictResult: LiveData<String?> = _predictResult

    private val _savings = MutableLiveData<Double>()
    val savings: LiveData<Double> = _savings

    fun getSavings() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getHome()
                _savings.value = response.showHome?.savings?.toDouble() ?: 0.0
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("HomeViewModel", "Home: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _savings.value = 0.0
                Log.e("HomeViewModel", "Home: ${errorResponse.message}")
            }
        }
    }

    fun getGoals() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getGoals()
                _goals.value = response.showGoal
                if (response.showGoal.isNotEmpty()) {
                    _predictResult.value = response.showGoal[0].predictGoal
                } else {
                    _predictResult.value = ""
                }
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("GoalsViewModel", "Goals: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isSuccess.value = false
                _goals.value = emptyList()
                Log.e("GoalsViewModel", "Goals: ${errorResponse.message}")
            }
        }
    }

    fun deleteGoal(id: String) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.deleteGoal(id)
                val updatedGoals = _goals.value?.toMutableList().also { it?.removeIf { goal -> goal.idGoal == id } }
                _goals.value = updatedGoals
                _isLoading.value = false
                _isSuccess.value = true
                _deleteResult.value = response.message
                Log.d("GoalsViewModel", "Delete Goal: ${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isSuccess.value = false
                _deleteResult.value = null
                Log.e("GoalsViewModel", "Delete Goal: ${errorResponse.message}")
            }
        }
    }

    fun predictGoal(predictRequest: PredictRequest) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.predictGoal(predictRequest)
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("GoalsViewModel", "Predict Goal: $response")
            } catch (e: Exception) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("GoalsViewModel", "Predict Goal: ${e.message}")
            }
        }
    }

    fun getPredict() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getPredict()
                _isLoading.value = false
                _isSuccess.value = true
                val result = withContext(Dispatchers.IO) {
                    response.showGoal[0].predictGoal
                }
                if (result != null) {
                    _predictResult.value = result
                }
                Log.d("GoalsViewModel", "Predict Goal: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _predictResult.value = null
                Log.e("GoalsViewModel", "Predict Goal: ${errorResponse.message}")
            } catch (e: JsonSyntaxException) {
                val errorBody = e.message.toString()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _predictResult.value = null
                Log.e("GoalsViewModel", "Predict Goal: ${errorResponse.message}")
            }
        }
    }
}