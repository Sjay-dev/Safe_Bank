package com.example.safebank.Model.Repository

import com.example.safebank.Model.Entities.TransferRequest
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Entities.UserResponse
import com.example.safebank.Model.Safe_Bank_Api.SafeBankApi
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: SafeBankApi
) {

    suspend fun getUserByAccountNumber(accountNumber: String): UserResponse {
        val response = api.getUserByAccountNumber(accountNumber)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("User not found")
        } else {
            throw Exception("Error: ${response.code()}")
        }
    }

    suspend fun performTransfer(
        accountNumber: String,
        amount: Double,
        description: String
    ): TransferResponse {
        return api.performTransfer(
            TransferRequest(
                receiverAccountNumber = accountNumber,
                amount = amount,
                description = description
            )
        )
    }

    suspend fun getTransferHistory(token: String): List<TransferResponse> {
        val response = api.getTransferHistory("Bearer $token")
        return response.content
    }

    suspend fun getTransactionWithNames(token: String): List<TransactionUI> {
        val transactions = api.getTransferHistory("Bearer $token").content

        return transactions.map { transaction ->

            val accountToFetch = if (transaction.transactionType == "CREDIT") {
                transaction.senderAccountNumber
            } else {
                transaction.receiverAccountNumber
            }

            val userResponse = api.getUserByAccountNumber(accountToFetch)

            val name = if (userResponse.isSuccessful) {
                userResponse.body()?.name ?: "Unknown"
            } else {
                "Unknown"
            }

            TransactionUI(
                name = name,
                amount = transaction.amount,
                isCredit = transaction.transactionType == "CREDIT",
                description = transaction.description
            )
        }
    }

    data class TransactionUI(
        val name: String,
        val amount: Double,
        val isCredit: Boolean,
        val description: String
    )
}