package com.example.safebank.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Safe_Bank_Api.RetrofitInstance
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {

    fun testConnection() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.ping()
                if (response.isSuccessful) {
                    Log.d("PING", response.body() ?: "No body")
                } else {
                    Log.d("PING", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("PING", "Failed: ${e.message}")
            }
        }
    }
}