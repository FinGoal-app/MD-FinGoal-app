package com.bangkit.fingoal.view.activities.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.Result
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError
    
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    /* fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        _isError.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postRegister(name, email, password)
                when {
                    response.error == false -> {
                        _isSuccess.value = true
                        _registerResult.value = Result.Success(response)
                    }
                    response.message != null -> {
                        throw Exception(response.message)
                    }
                }
            } catch (e: HttpException) {
                val errorBody = Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                val errorMessage = errorBody?.message ?: e.message()
                _isError.value = errorMessage.toString()
                _registerResult.value = Result.Error(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    } */
}