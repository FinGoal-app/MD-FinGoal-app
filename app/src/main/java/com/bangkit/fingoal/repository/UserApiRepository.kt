package com.bangkit.fingoal.repository

import com.bangkit.fingoal.data.model.UserLoginInfo
import com.bangkit.fingoal.data.model.UserRegisterInfo
import com.bangkit.fingoal.data.remote.apiService.UserApi
import com.bangkit.fingoal.data.remote.response.UserLoginResponse
import com.bangkit.fingoal.data.remote.response.UserRegisterResponse
import com.bangkit.fingoal.view.common.UiState
import javax.inject.Inject

class UserApiRepository @Inject constructor(
    private val userApi: UserApi
) {

    suspend fun userLogin(userLoginInfo: UserLoginInfo): UiState<UserLoginResponse> {
        val response = try {
            userApi.userLogin(userLoginInfo)
        } catch (e: Exception) {
            return UiState.Error(message = e.message ?: "User authentication failed")
        }
        return UiState.Success(response)
    }

    suspend fun userRegister(userRegisterInfo: UserRegisterInfo): UiState<UserRegisterResponse> {
        val response = try {
            userApi.userRegister(userRegisterInfo)
        } catch (e: Exception) {
            return UiState.Error(message = e.message ?: "User registration failed")
        }
        return UiState.Success(response)
    }
}