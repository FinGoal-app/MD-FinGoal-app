package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class GetAllocationResponse(

	@field:SerializedName("showAllocation")
	val showAllocation: List<ShowAllocationItem> = listOf(),

	@field:SerializedName("message")
	val message: String? = null
)

data class ShowAllocationItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("id_allocation")
	val idAllocation: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null
)
