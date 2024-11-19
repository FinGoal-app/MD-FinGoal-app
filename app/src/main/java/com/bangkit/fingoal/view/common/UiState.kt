package com.bangkit.fingoal.view.common

sealed class UiState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T, message: String? = null): UiState<T>(data, message)
    class Error<T>(data: T? = null, message: String): UiState<T>(data, message)
    class Loading<T>(): UiState<T>()
}