package com.bangkit.fingoal.view.activities.budget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.retrofit.AddAllocationRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BudgetFormViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _allocationResult = MutableLiveData<String?>()
    val allocationResult: LiveData<String?> = _allocationResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addAllocation(addAllocationRequest: AddAllocationRequest) {
        _isLoading.value = true
        _allocationResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.addAllocation(addAllocationRequest)
                _allocationResult.value = response.message
                _isSuccess.value = true
                _isLoading.value = false
                Log.d("Goal", response.message.toString())
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isSuccess.value = false
                _isLoading.value = false
                _allocationResult.value = errorResponse.message
                Log.d("Goal", errorResponse.message.toString())
            }
        }
    }
}