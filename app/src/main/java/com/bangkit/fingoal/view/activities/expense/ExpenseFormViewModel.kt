package com.bangkit.fingoal.view.activities.expense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.retrofit.AddExpenseRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ExpenseFormViewModel (private val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _expenseResult = MutableLiveData<String?>()
    val expenseResult: LiveData<String?> = _expenseResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addExpense(addExpenseRequest: AddExpenseRequest) {
        _isLoading.value = true
        _expenseResult.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.postExpense(addExpenseRequest)
                _expenseResult.value = response.message
                _isSuccess.value = true
                _isLoading.value = false
                Log.d("Expense", "${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _expenseResult.value = errorResponse.message
                _isSuccess.value = false
                _isLoading.value = false
                Log.d("Expense", errorResponse.message)
            }
        }
    }
}