package com.bangkit.fingoal.data.remote.apiService

import com.bangkit.fingoal.data.model.UserLoginInfo
import com.bangkit.fingoal.data.model.UserRegisterInfo
import com.bangkit.fingoal.data.remote.response.UserLoginResponse
import com.bangkit.fingoal.data.remote.response.UserRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user/login")
    suspend fun userLogin(
        @Body userLoginInfo: UserLoginInfo
    ): UserLoginResponse

    @POST("user/signup")
    suspend fun userRegister(
        @Body userRegisterInfo: UserRegisterInfo
    ): UserRegisterResponse

    companion object {
        const val BASE_URL = ""
    }
}