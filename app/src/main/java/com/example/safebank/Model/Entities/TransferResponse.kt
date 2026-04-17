package com.example.safebank.Model.Entities

data class TransferResponse(
    val transferId: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val senderName: String,
    val receiverName: String,
    val amount: Double,
    val description: String,
    val status: String,
    val createdAt: String,
    val transactionType: String
)
