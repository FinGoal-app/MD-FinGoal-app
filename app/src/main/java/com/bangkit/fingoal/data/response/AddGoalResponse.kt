package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AddGoalResponse(

	@field:SerializedName("addGoal")
	val addGoal: AddGoal? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddGoal(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("goal")
	val goal: String? = null,

	@field:SerializedName("id_goal")
	val idGoal: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("target")
	val target: String? = null
)
