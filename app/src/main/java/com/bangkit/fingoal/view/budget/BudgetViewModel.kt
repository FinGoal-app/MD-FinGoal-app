package com.bangkit.fingoal.view.budget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.AllocationPredictRequest
import com.bangkit.fingoal.data.response.AllocationPredictResponse
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.response.ShowAllocationItem
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BudgetViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _budgetResult = MutableLiveData<List<ShowAllocationItem>>()
    val budgetResult: LiveData<List<ShowAllocationItem>> = _budgetResult

    private val _allocation = MutableLiveData<Double>()
    val allocation: LiveData<Double> = _allocation

    private val _deleteResult = MutableLiveData<String?>()
    val deleteResult: LiveData<String?> = _deleteResult

    private val _predictResult = MutableLiveData<AllocationPredictResponse?>()
    val predictResult: LiveData<AllocationPredictResponse?> = _predictResult

    private val _foodsPredict = MutableLiveData<Any?>()
    val foodsPredict = MutableLiveData<Any?>()

    private val _transportPredict = MutableLiveData<Any?>()
    val transportPredict = MutableLiveData<Any?>()

    private val _electricityPredict = MutableLiveData<Any?>()
    val electricityPredict = MutableLiveData<Any?>()

    private val _idUser = MutableLiveData<String>()
    val idUser: LiveData<String> = _idUser

    fun getBudget() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getAllocation()
                _budgetResult.value = response.showAllocation
                if (response.showAllocation.isNotEmpty()) {
                    _idUser.value = response.showAllocation[0].idUser.toString()
                } else {
                    _idUser.value = ""
                }
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("BudgetViewModel", "Get Budget: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _budgetResult.value = emptyList()
                Log.e("BudgetViewModel", "Get Budget: ${errorResponse.message}")
            }
        }
    }

    fun getAllocationAmount() {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.getHome()
                _allocation.value = response.showHome?.amountAllocation?.toDouble() ?: 0.0
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("BudgetViewModel", "Get Allocation: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _isLoading.value = false
                _isSuccess.value = false
                _allocation.value = 0.0
                Log.e("BudgetViewModel", "Get Allocation: ${errorResponse.message}")
            }
        }
    }

    fun deleteBudget(id: String) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.deleteAllocation(id)
                _deleteResult.value = response.message
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("BudgetViewModel", "Delete Budget: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _deleteResult.value = null
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("BudgetViewModel", "Delete Budget: ${errorResponse.message}")
            }
        }
    }

    fun predictAllocation(allocationPredictRequest: AllocationPredictRequest) {
        _isLoading.value = true
        _isSuccess.value = false
        viewModelScope.launch {
            try {
                val response = userRepository.predictAllocation(allocationPredictRequest)
                _predictResult.value = response
                _foodsPredict.value = response.idealFood
                _transportPredict.value = response.idealTransportation
                _electricityPredict.value = response.idealElectricity
                _isLoading.value = false
                _isSuccess.value = true
                Log.d("BudgetViewModel", "Predict Allocation: $response")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _foodsPredict.value = null
                _transportPredict.value = null
                _electricityPredict.value = null
                _predictResult.value = null
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("BudgetViewModel", "Predict Allocation: ${errorResponse.message}")
            }
        }
    }
}