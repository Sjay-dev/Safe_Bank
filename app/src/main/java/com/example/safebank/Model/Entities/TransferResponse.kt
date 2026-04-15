package com.example.safebank.Model.Entities

data class TransferResponse(
    val transferId: Int,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: Double,
    val description: String,
    val status: String,
    val createdAt: String
)
