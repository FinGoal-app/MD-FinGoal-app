package com.bangkit.fingoal.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.fingoal.data.model.UserRegisterInfo
import com.bangkit.fingoal.data.remote.response.UserRegisterResponse
import com.bangkit.fingoal.repository.UserApiRepository
import com.bangkit.fingoal.view.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userApiRepository: UserApiRepository
): ViewModel() {

    private val _userToSignUpState: MutableStateFlow<UiState<UserRegisterResponse>?> =
        MutableStateFlow(null)
    val userToSignUpState = _userToSignUpState.asStateFlow()

    private val _userToSignUpInfo: MutableStateFlow<UserRegisterInfo> =
        MutableStateFlow(
            UserRegisterInfo(
                fullName = "",
                username = "",
                email = "",
                password = ""
            )
        )
    val userToSignUpInfo = _userToSignUpInfo.asStateFlow()

    fun signUpUser() {
        _userToSignUpState.value = UiState.Loading()
        viewModelScope.launch {
            _userToSignUpState.value = userApiRepository.userRegister(_userToSignUpInfo.value)
        }
    }

    fun updateUserSignUpInfo(userToSignUpInfo: UserRegisterInfo) {
        _userToSignUpInfo.value = userToSignUpInfo
    }
}