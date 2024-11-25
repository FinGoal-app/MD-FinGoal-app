package com.bangkit.fingoal.data.retrofit

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email", encoded = true) email: String,
        @Field("password", encoded = true) password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email", encoded = true) email: String,
        @Field("password", encoded = true) password: String,
    ): RegisterResponse
}