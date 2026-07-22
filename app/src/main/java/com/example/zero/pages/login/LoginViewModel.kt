package com.example.zero.pages.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zero.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var guardarSesion by mutableStateOf(false)

    fun fetchLogin() {
        viewModelScope.launch {
            _uiState.value = LoginUIState.Loading
            try {
                val response = RetrofitClient.loginService.login(email, password)
                _uiState.value = LoginUIState.Success(response)
            } catch (e: Exception) {
                _uiState.value = LoginUIState.Error("Error: ${e.localizedMessage}")
            }
        }
    }
}