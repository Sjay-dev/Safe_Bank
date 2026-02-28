package com.example.safebank.Model.Entities

data class AuthResponse( val token: String,
                         val tokenType: String,
                         val email: String,
                         val accountNumber: Long)
