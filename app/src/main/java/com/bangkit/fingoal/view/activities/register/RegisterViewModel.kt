package com.bangkit.fingoal.view.activities.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.retrofit.RegisterRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _registerResult = MutableLiveData<String?>()
    val registerResult: LiveData<String?> = _registerResult

    fun register(registerRequest: RegisterRequest) {
        _isLoading.value = true
        _registerResult.value = null
        viewModelScope.launch {
           try {
               val response = userRepository.postRegister(registerRequest)
               _isLoading.value = false
               _isSuccess.value = true
               _registerResult.value = response.message
               Log.d("RegisterViewModel", "Register: ${response.message}")
           } catch (e: HttpException) {
               val errorBody = e.response()?.errorBody()?.string()
               val gson = Gson()
               val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
               _registerResult.value = errorResponse.message
               _isLoading.value = false
               _isSuccess.value = false
               Log.e("RegisterViewModel", "Register: ${errorResponse.message}")
           }
        }
    }
}