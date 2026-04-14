package com.example.safebank.Model.Repository

import com.example.safebank.Model.Entities.UserResponse
import com.example.safebank.Model.Safe_Bank_Api.SafeBankApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: SafeBankApi
) {

    suspend fun getUserByAccountNumber(accountNumber: String): UserResponse {
        val response = api.getUserByAccountNumber(accountNumber)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("User not found")
        } else {
            throw Exception("Error: ${response.code()}")
        }
    }
}