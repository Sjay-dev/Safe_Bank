package com.example.safebank.Navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object SignUpRoute

@Serializable
object TransferRoute

@Serializable
data class TransferDetailsRoute(
    val accountNumber: String,
    val recipientName: String
)

@Serializable
data class MainRoute(
    val name: String,
    val accountNumber: String,
    val balance: String
)



