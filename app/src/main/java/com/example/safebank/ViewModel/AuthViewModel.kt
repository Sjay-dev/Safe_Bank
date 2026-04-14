package com.example.safebank.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Repository.AuthRepository
import com.example.safebank.Model.Safe_Bank_Api.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val  tokenProvider: TokenProvider
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            try {
                val response = repository.login(email, password)


                tokenProvider.saveToken(response.token)


                uiState = AuthUiState.LoginSuccess(response.name, response.accountNumber.toString(), response.balance, response.token)
            } catch (e: Exception) {
                uiState = AuthUiState.Error("Login failed")
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            try {
                val response = repository.loginWithGoogle(idToken)
                uiState = AuthUiState.LoginSuccess(response.name, response.accountNumber.toString(), response.balance, response.token)
            } catch (e: Exception) {
                uiState = AuthUiState.Error(e.message ?: "Google sign-in failed")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                uiState = AuthUiState.Loading
                repository.register(name, email, password)
                uiState = AuthUiState.RegisterSuccess
            } catch (e: Exception) {
                uiState = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun resetState() {
        uiState = AuthUiState.Idle
    }
}