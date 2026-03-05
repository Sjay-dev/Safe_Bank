package com.example.safebank.Model.Entities

data class AuthResponse( val token: String,
                         val name: String,
                         val email: String,
                         val accountNumber: Long)
