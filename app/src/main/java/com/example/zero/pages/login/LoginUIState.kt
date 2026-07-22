package com.example.zero.pages.login

import com.example.zero.models.LoginResponse

sealed interface LoginUIState {
    data object Idle : LoginUIState
    data object Loading : LoginUIState
    data class Success(val resultado: LoginResponse) : LoginUIState
    data class Error(val message: String) : LoginUIState
}