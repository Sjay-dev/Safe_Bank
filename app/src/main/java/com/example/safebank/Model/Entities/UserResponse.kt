package com.example.safebank.Model.Entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class UserResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val accountNumber: String,
    val balance: BigDecimal,
    val createdAt: String
)
