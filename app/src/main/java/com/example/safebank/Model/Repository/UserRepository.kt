package com.example.safebank.Model.Repository

import android.util.Log
import com.example.safebank.Model.Entities.TransferRequest
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Entities.UserResponse
import com.example.safebank.Model.Safe_Bank_Api.SafeBankApi
import javax.inject.Inject
import com.example.safebank.Model.Entities.Bank
import com.example.safebank.Model.Entities.AccountResolution
import com.example.safebank.Model.Entities.ExternalTransferRequest
import com.example.safebank.Model.Entities.ExternalTransferResponse

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
        val response = api.getTransferHistory()
        return response.content
    }

    suspend fun getBanks(): List<Bank> {
        val response = api.getBanks()

        Log.d("BANKS_HTTP", "Code = ${response.code()}")
        Log.d("BANKS_HTTP", "Body = ${response.body()}")

        return response.bodyOrThrow(0)
    }

    suspend fun resolveBankAccount(accountNumber: String, bankCode: String): AccountResolution {
        val response = api.resolveBankAccount(accountNumber, bankCode)
        return response.bodyOrThrow(response.code())
    }

    suspend fun performExternalTransfer(
        accountNumber: String,
        bankCode: String,
        amount: Double,
        narration: String
    ): ExternalTransferResponse {
        val response = api.performExternalTransfer(
            ExternalTransferRequest(amount, bankCode, accountNumber, narration)
        )
        return response.bodyOrThrow(response.code())
    }

    private fun <T> retrofit2.Response<T>.bodyOrThrow(code: Int): T {
        if (isSuccessful) return body() ?: throw IllegalStateException("The server returned an empty response")
        throw IllegalStateException(errorBody()?.string()?.takeIf { it.isNotBlank() }
            ?: "Request failed (HTTP $code)")
    }

    suspend fun getTransactionWithNames(token: String): List<TransactionUI> {
        val transactions = api.getTransferHistory().content

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
