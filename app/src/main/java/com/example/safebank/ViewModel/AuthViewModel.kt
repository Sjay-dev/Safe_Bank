package com.example.safebank.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    fun login(email: String, password: String) {

        viewModelScope.launch {

            uiState = AuthUiState.Loading

            try {

                val response = repository.login(email, password)

                uiState = AuthUiState.LoginSuccess(response.name)

            } catch (e: Exception) {

                uiState = AuthUiState.Error("Login failed")

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