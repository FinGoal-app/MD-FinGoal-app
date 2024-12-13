package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("showHome")
	val showHome: ShowHome? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ShowHome(

	@field:SerializedName("amount_allocation")
	val amountAllocation: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("savings")
	val savings: Int? = null,

	@field:SerializedName("resultHistory")
	val resultHistory: List<ResultHistoryItem> = listOf()
)

data class ResultHistoryItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_history")
	val idHistory: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("label")
	val label: String? = null
)
