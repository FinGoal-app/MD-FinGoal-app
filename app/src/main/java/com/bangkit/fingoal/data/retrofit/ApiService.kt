package com.bangkit.fingoal.data.retrofit

import com.bangkit.fingoal.data.response.AddAllocationResponse
import com.bangkit.fingoal.data.response.AddExpenseResponse
import com.bangkit.fingoal.data.response.AddGoalResponse
import com.bangkit.fingoal.data.response.AddIncomeResponse
import com.bangkit.fingoal.data.response.AddSavingResponse
import com.bangkit.fingoal.data.response.DeleteBudgetResponse
import com.bangkit.fingoal.data.response.DeleteGoalResponse
import com.bangkit.fingoal.data.response.GetAllocationResponse
import com.bangkit.fingoal.data.response.GetGoalsResponse
import com.bangkit.fingoal.data.response.HomeResponse
import com.bangkit.fingoal.data.response.LoginResponse
import com.bangkit.fingoal.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(
        @Body
        loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body
        registerRequest: RegisterRequest
    ): RegisterResponse

    // Home
    @GET("home")
    suspend fun getHome(
    ): HomeResponse

    @POST("money/income")
    suspend fun addIncome(
        @Body
        addIncomeRequest: AddIncomeRequest
    ): AddIncomeResponse

    @POST("money/expense")
    suspend fun addExpense(
        @Body
        addExpenseRequest: AddExpenseRequest
    ): AddExpenseResponse

    @POST("money/saving")
    suspend fun addSaving(
        @Body
        addSavingsRequest: AddSavingRequest
    ): AddSavingResponse

    // Goal
    @POST("money/goal")
    suspend fun goal(
        @Body
        addGoalRequest: AddGoalRequest
    ): AddGoalResponse

    @GET("money/goal")
    suspend fun getGoals(
    ): GetGoalsResponse

    @PUT("money/goal/{id}")
    suspend fun updateGoal(
        @Path("id") id: String,
        @Body
        updateGoalRequest: UpdateGoalRequest
    ): AddGoalResponse

    @GET("money/goal")
    suspend fun getPredict(
    ): GetGoalsResponse

    @DELETE("money/goal/{id}")
    suspend fun deleteGoal(
        @Path("id") id: String
    ): DeleteGoalResponse

    // Budget
    @POST("money/allocation")
    suspend fun addAllocation(
        @Body
        addAllocationRequest: AddAllocationRequest
    ): AddAllocationResponse

    @GET("money/allocation")
    suspend fun getAllocation(
    ): GetAllocationResponse

    @PUT("money/allocation/{id}")
    suspend fun updateAllocation(
        @Path("id") id: String,
        @Body
        updateAllocationRequest: UpdateAllocationRequest
    ): AddAllocationResponse

    @DELETE("money/allocation/{id}")
    suspend fun deleteAllocation(
        @Path("id") id: String
    ): DeleteBudgetResponse
}