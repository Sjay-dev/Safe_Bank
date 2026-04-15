package com.example.safebank.Model.Entities

data class TransferRequest(
    val receiverAccountNumber: String,
    val amount: Double,
    val description: String
)
