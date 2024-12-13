package com.bangkit.fingoal.data.repository

import com.bangkit.fingoal.data.pref.UserModel
import com.bangkit.fingoal.data.pref.UserPreference
import com.bangkit.fingoal.data.response.AddAllocationResponse
import com.bangkit.fingoal.data.response.AddExpenseResponse
import com.bangkit.fingoal.data.response.AddGoalResponse
import com.bangkit.fingoal.data.response.AddIncomeResponse
import com.bangkit.fingoal.data.response.AddSavingResponse
import com.bangkit.fingoal.data.response.AllocationPredictRequest
import com.bangkit.fingoal.data.response.AllocationPredictResponse
import com.bangkit.fingoal.data.response.DeleteBudgetResponse
import com.bangkit.fingoal.data.response.DeleteGoalResponse
import com.bangkit.fingoal.data.response.GetAllocationResponse
import com.bangkit.fingoal.data.response.GetGoalsResponse
import com.bangkit.fingoal.data.response.HomeResponse
import com.bangkit.fingoal.data.response.LoginResponse
import com.bangkit.fingoal.data.response.RegisterResponse
import com.bangkit.fingoal.data.retrofit.AddAllocationRequest
import com.bangkit.fingoal.data.retrofit.AddExpenseRequest
import com.bangkit.fingoal.data.retrofit.AddGoalRequest
import com.bangkit.fingoal.data.retrofit.AddIncomeRequest
import com.bangkit.fingoal.data.retrofit.AddSavingRequest
import com.bangkit.fingoal.data.retrofit.ApiService
import com.bangkit.fingoal.data.retrofit.ApiServiceML
import com.bangkit.fingoal.data.retrofit.LoginRequest
import com.bangkit.fingoal.data.retrofit.PredictRequest
import com.bangkit.fingoal.data.retrofit.RegisterRequest
import com.bangkit.fingoal.data.retrofit.UpdateAllocationRequest
import com.bangkit.fingoal.data.retrofit.UpdateGoalRequest
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiService: ApiService,
    private val apiServiceML: ApiServiceML,
    private val pref: UserPreference
) {
    suspend fun postRegister(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }

    suspend fun postLogin(loginRequest: LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }

    suspend fun getHome(): HomeResponse {
        return apiService.getHome()
    }

    suspend fun postIncome(addIncomeRequest: AddIncomeRequest): AddIncomeResponse {
        return apiService.addIncome(addIncomeRequest)
    }

    suspend fun postExpense(addExpenseRequest: AddExpenseRequest): AddExpenseResponse {
        return apiService.addExpense(addExpenseRequest)
    }

    suspend fun postSaving(addSavingsRequest: AddSavingRequest): AddSavingResponse {
        return apiService.addSaving(addSavingsRequest)
    }

    suspend fun getGoals(): GetGoalsResponse {
        return apiService.getGoals()
    }

    suspend fun postGoal(addGoalRequest: AddGoalRequest): AddGoalResponse {
        return apiService.goal(addGoalRequest)
    }

    suspend fun predictGoal(predictRequest: PredictRequest): GetGoalsResponse {
        return apiServiceML.predictGoal(predictRequest)
    }

    suspend fun predictAllocation(allocationPredictRequest: AllocationPredictRequest): AllocationPredictResponse {
        return apiServiceML.predictAllocation(allocationPredictRequest)
    }

    suspend fun getPredict(): GetGoalsResponse {
        return apiService.getPredict()
    }

    suspend fun updateGoal(id: String, updateGoalRequest: UpdateGoalRequest): AddGoalResponse {
        return apiService.updateGoal(id, updateGoalRequest)
    }

    suspend fun deleteGoal(id: String): DeleteGoalResponse {
        return apiService.deleteGoal(id)
    }

    suspend fun addAllocation(addAllocationRequest: AddAllocationRequest): AddAllocationResponse {
        return apiService.addAllocation(addAllocationRequest)
    }

    suspend fun getAllocation(): GetAllocationResponse {
        return apiService.getAllocation()
    }

    suspend fun updateAllocation(id: String, updateAllocationRequest: UpdateAllocationRequest): AddAllocationResponse {
        return apiService.updateAllocation(id, updateAllocationRequest)
    }

    suspend fun deleteAllocation(id: String): DeleteBudgetResponse {
        return apiService.deleteAllocation(id)
    }

    fun getLoginState(): Flow<UserModel> {
        return pref.getSession()
    }

    fun getToken(): Flow<String> {
        return pref.getToken()
    }

    fun getUsername(): Flow<String> {
        return pref.getUsername()
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiServiceML: ApiServiceML,
            apiService: ApiService,
            pref: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, apiServiceML, pref)
            }.also { instance = it }
    }
}