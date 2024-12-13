package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AddSavingResponse(

	@field:SerializedName("addSaving")
	val addSaving: AddSaving? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AddSaving(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id_user")
	val idUser: String? = null,

	@field:SerializedName("savings")
	val savings: Int? = null,

	@field:SerializedName("label")
	val label: String? = null
)
