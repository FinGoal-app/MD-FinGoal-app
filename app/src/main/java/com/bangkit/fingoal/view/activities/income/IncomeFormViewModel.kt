package com.bangkit.fingoal.view.activities.income

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.retrofit.AddIncomeRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class IncomeFormViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _incomeResult = MutableLiveData<String?>()
    val incomeResult: LiveData<String?> = _incomeResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addIncome(addIncomeRequest: AddIncomeRequest) {
        _isLoading.value = true
        _incomeResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postIncome(addIncomeRequest)
                _incomeResult.value = response.message
                _isSuccess.value = true
                _isLoading.value = false
                Log.d("Income", "Income: ${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isSuccess.value = false
                _isLoading.value = false
                _incomeResult.value = errorResponse.message
                Log.d("Income", "Error: ${errorResponse.message}")
            }
        }
    }
}