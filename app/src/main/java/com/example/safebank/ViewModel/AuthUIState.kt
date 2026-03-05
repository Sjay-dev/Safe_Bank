package com.example.safebank.ViewModel

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object LoginSuccess : AuthUiState()
    object RegisterSuccess : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}