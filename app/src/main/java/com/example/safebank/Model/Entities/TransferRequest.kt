package com.example.safebank.Model.Entities

data class TransferRequest(
    val senderAccountNumber: Long,
    val receiverAccountNumber: Long,
    val amount: Double,
    val description: String
)
