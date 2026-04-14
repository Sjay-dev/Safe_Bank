package com.example.safebank.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransferViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var recipientName by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun fetchUser(accountNumber: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null

                val user = repository.getUserByAccountNumber(accountNumber)

                recipientName = user.name
            } catch (e: Exception) {
                error = e.message ?: "Account not found"
                recipientName = null
            } finally {
                isLoading = false
            }
        }
    }
}