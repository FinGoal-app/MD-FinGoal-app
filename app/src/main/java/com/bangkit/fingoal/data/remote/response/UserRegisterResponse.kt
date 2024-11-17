package com.bangkit.fingoal.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserRegisterResponse (
    val error: String,
    val message: String,
    val registerResult: RegisterResult
)

data class RegisterResult (
    val signupToken: SignUpToken
)

data class SignUpToken (
    @SerializedName("access token")
    val accessToken: String
)