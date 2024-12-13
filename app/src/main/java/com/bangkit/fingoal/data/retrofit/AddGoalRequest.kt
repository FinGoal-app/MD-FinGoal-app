package com.bangkit.fingoal.data.retrofit

data class AddGoalRequest(
    val goal: String,
    val amount: Int,
    val target: String,
    val description: String
)
