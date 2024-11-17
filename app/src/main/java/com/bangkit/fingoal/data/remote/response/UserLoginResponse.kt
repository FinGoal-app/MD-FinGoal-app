package com.bangkit.fingoal.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    val error: String,
    val loginResult: LoginResult,
    val message: String
)

data class LoginResult(
    val token: Token,
    val userId: UserId
)

data class Token(
    @SerializedName("access token")
    val accessToken: String
)

data class UserId(
    @SerializedName("username")
    val username: String,
    val id: Int
)

