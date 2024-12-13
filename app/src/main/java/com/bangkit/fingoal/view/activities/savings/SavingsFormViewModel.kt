package com.bangkit.fingoal.view.activities.savings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.response.ShowGoalItem
import com.bangkit.fingoal.data.retrofit.AddSavingRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SavingsFormViewModel (private val userRepository: UserRepository): ViewModel() {

    private val _savingResult = MutableLiveData<String?>()
    val savingResult: LiveData<String?> = _savingResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _goals = MutableLiveData<List<ShowGoalItem>>()
    val goals: LiveData<List<ShowGoalItem>> = _goals

    fun addSaving(addSavingRequest: AddSavingRequest) {
        _isLoading.value = true
        _savingResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postSaving(addSavingRequest)
                _savingResult.value = response.message
                _isSuccess.value = true
                _isLoading.value = false
                Log.d("Saving", "${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isSuccess.value = false
                _isLoading.value = false
                _savingResult.value = errorResponse.message
                Log.d("Saving", errorResponse.message)
            }
        }
    }

    fun getGoals() {
        viewModelScope.launch {
            try {
                val response = userRepository.getGoals()
                _goals.value = response.showGoal
                Log.d("Goals", "${response.showGoal}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _goals.value = emptyList()
                Log.e("GoalsViewModel", "Goals: ${errorResponse.message}")
            }
        }
    }
}