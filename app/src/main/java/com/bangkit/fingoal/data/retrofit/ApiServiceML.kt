package com.bangkit.fingoal.data.retrofit

import com.bangkit.fingoal.data.response.AllocationPredictRequest
import com.bangkit.fingoal.data.response.AllocationPredictResponse
import com.bangkit.fingoal.data.response.GetGoalsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceML {

    @POST("money/goal/predict")
    suspend fun predictGoal(
        @Body
        predictRequest: PredictRequest
    ): GetGoalsResponse

    @POST("money/allocation/predict")
    suspend fun predictAllocation(
        @Body
        allocationPredictRequest: AllocationPredictRequest
    ): AllocationPredictResponse

}