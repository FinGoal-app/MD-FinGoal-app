package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AddExpenseResponse(

	@field:SerializedName("addExpense")
	val addExpense: AddExpense? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddExpense(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_expense")
	val idExpense: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("savings")
	val savings: Int? = null,

	@field:SerializedName("tujuan")
	val tujuan: String? = null
)
