package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AddIncomeResponse(

	@field:SerializedName("addIncome")
	val addIncome: AddIncome? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddIncome(

	@field:SerializedName("id_incomes")
	val idIncomes: String? = null,

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("sumber")
	val sumber: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("savings")
	val savings: Int? = null
)
