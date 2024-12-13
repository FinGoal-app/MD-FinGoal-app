package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class GetGoalsResponse(

	@field:SerializedName("showGoal")
	val showGoal: List<ShowGoalItem> = listOf(),

	@field:SerializedName("message")
	val message: String? = null
)

data class ShowGoalItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("goal")
	val goal: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_goal")
	val idGoal: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("finished")
	val finished: Int? = null,

	@field:SerializedName("saving_goal")
	val savingGoal: Int? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("target")
	val target: String? = null,

	@field:SerializedName("predict_goal")
	val predictGoal: String? = null
)
