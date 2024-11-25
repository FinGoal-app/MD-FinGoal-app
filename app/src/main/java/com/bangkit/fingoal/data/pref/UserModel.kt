package com.bangkit.fingoal.data.pref

data class UserModel(
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)