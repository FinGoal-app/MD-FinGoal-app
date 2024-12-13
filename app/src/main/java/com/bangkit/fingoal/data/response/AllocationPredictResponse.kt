package com.bangkit.fingoal.data.response

import com.google.gson.annotations.SerializedName

data class AllocationPredictResponse(

	@field:SerializedName("idealTransportation")
	val idealTransportation: Any? = null,

	@field:SerializedName("idealFood")
	val idealFood: Any? = null,

	@field:SerializedName("totalIdeal")
	val totalIdeal: Any? = null,

	@field:SerializedName("totalUser")
	val totalUser: Int? = null,

	@field:SerializedName("idealElectricity")
	val idealElectricity: Any? = null,

	@field:SerializedName("message")
	val message: String? = null
)
