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
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var balance by mutableStateOf("0.00")
        private set

    fun refreshBalance(accountNumber: String) {
        viewModelScope.launch {
            try {
                val user = repository.getUserByAccountNumber(accountNumber)
                balance = user.balance.toString()
            } catch (e: Exception) {

            }
        }
    }
}