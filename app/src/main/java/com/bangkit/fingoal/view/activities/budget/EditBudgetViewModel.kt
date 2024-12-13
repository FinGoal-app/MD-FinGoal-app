package com.bangkit.fingoal.view.activities.budget

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.data.response.ErrorResponse
import com.bangkit.fingoal.data.retrofit.UpdateAllocationRequest
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class EditBudgetViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _updateBudgetResult = MutableLiveData<String?>()
    val updateBudgetResult: MutableLiveData<String?> = _updateBudgetResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun updateBudget(id: String, updateAllocationRequest: UpdateAllocationRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.updateAllocation(id, updateAllocationRequest)
                _updateBudgetResult.value = response.message
                _isLoading.value = false
                Log.d("EditBudgetViewModel", "EditBudget: ${response.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _updateBudgetResult.value = errorResponse.message
                _isLoading.value = false
                Log.e("EditBudgetViewModel", "EditBudget: ${errorResponse.message}")
            }
        }
    }
}