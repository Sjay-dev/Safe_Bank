package com.example.safebank.ViewModel

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class LoginSuccess(val name: String, val accountNumber: String, val balance: String, val token: String) : AuthUiState()
    object RegisterSuccess : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}