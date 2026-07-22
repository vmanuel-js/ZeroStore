package com.example.zero.pages.initial

sealed interface InitialUIState {
    data object Loading : InitialUIState
    data class Success(val data: InitialData) : InitialUIState
    data class Error(val message: String) : InitialUIState
}