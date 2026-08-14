package com.example.safebank.Navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object SignUpRoute

@Serializable
object TransferRoute

@Serializable
object ExternalTransferRoute

@Serializable
data class TransferDetailsRoute(
    val accountNumber: String,
    val recipientName: String,
    val senderAccountNumber: String
)

@Serializable
data class TransactionReceiptRoute(
    val amount: String,
    val recipientName: String,
    val recipientBank: String,
    val recipientAccount: String,
    val narration: String,
    val reference: String,
    val dateTime: String,
    val status: String
)

@Serializable
data class MainRoute(
    val name: String,
    val accountNumber: String,
    val balance: String ,
    val token : String
)



