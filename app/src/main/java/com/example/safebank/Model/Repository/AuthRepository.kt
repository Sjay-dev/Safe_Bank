package com.example.safebank.Model.Repository

import com.example.safebank.Model.Entities.AuthRequest
import com.example.safebank.Model.Entities.AuthResponse
import com.example.safebank.Model.Entities.UserRequest
import com.example.safebank.Model.Safe_Bank_Api.SafeBankApi
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: SafeBankApi
) {

    suspend fun login(email: String, password: String): AuthResponse {
        return api.login(AuthRequest(email = email, password = password))
    }

    suspend fun register(name: String, email: String, password: String): AuthResponse {
        return api.register(UserRequest(name, email, password))
    }
}