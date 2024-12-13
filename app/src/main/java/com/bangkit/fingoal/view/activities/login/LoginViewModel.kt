package com.bangkit.fingoal.view.activities.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.pref.UserModel
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.response.User
import com.bangkit.fingoal.data.retrofit.LoginRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    fun login(loginRequest: LoginRequest) {
        _isLoading.value = true
        _loginResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postLogin(loginRequest)
                if (response.user != null) {
                    saveSession(
                        UserModel(
                            response.user.idUser.toString(),
                            response.user.nama.toString(),
                            response.user.token.toString(),
                            true
                        )
                    )
                }
                _isLoading.value = false
                _isSuccess.value = true
                _loginResult.value = response.message
                Log.d("LoginViewModel", "Login: ${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _loginResult.value = errorResponse.message
                Log.e("LoginViewModel", "Login: ${errorResponse.message}")
            } catch (e: SocketTimeoutException) {
                _isLoading.value = false
                _isSuccess.value = false
                _loginResult.value = e.message
                Log.e("LoginViewModel", "Login: ${e.message}")
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getLoginState().asLiveData()
    }
}