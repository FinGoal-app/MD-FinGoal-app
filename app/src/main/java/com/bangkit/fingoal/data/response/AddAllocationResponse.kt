package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AddAllocationResponse(

	@field:SerializedName("addAllocation")
	val addAllocation: AddAllocation? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddAllocation(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("amount_allocation")
	val amountAllocation: Int? = null,

	@field:SerializedName("id_allocation")
	val idAllocation: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null
)
