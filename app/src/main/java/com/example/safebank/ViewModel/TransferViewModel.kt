package com.example.safebank.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransferViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var recipientName by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var transferResult by mutableStateOf<TransferResponse?>(null)
        private set

    // ✅ Fetch user (already good)
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

    // ADD THIS (THIS IS YOUR MISSING PIECE)
    fun performTransfer(
        accountNumber: String,
        amount: Double,
        remark: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                error = null

                transferResult = repository.performTransfer(
                    accountNumber = accountNumber,
                    amount = amount,
                    description = remark
                )

            } catch (e: Exception) {
                error = e.message ?: "Transfer failed"
            } finally {
                isLoading = false
            }
        }
    }

    var transactions by mutableStateOf<List<TransferResponse>>(emptyList())
        private set

    fun loadTransactions(token: String) {
        viewModelScope.launch {
            try {
                transactions = repository.getTransferHistory(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}