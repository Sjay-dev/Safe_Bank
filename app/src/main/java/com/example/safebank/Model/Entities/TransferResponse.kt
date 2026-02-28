package com.example.safebank.Model.Entities

data class TransferResponse(
    val id: Long,
    val senderAccountNumber: Long,
    val receiverAccountNumber: Long,
    val amount: Double,
    val description: String,
    val status: String,
    val createdAt: String
)
