package com.bangkit.fingoal.data.pref

data class UserModel(
    val idUser: String,
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)