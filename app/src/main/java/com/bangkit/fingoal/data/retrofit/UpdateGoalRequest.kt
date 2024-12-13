package com.bangkit.fingoal.data.retrofit

data class UpdateGoalRequest(
    val goal: String,
    val amount: Int,
    val target: String,
    val description: String
)
