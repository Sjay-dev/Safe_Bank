package com.example.safebank.Model.Entities

import com.google.gson.annotations.SerializedName

data class Bank(
    val name: String,
    @SerializedName(value = "code", alternate = ["bankCode", "bank_code"])
    val code: String,
    val active: Boolean = true
)

data class AccountResolution(
    @SerializedName(value = "accountName", alternate = ["account_name", "name"])
    val accountName: String,
    @SerializedName(value = "accountNumber", alternate = ["account_number"])
    val accountNumber: String? = null,
    @SerializedName(value = "bankCode", alternate = ["bank_code", "code"])
    val bankCode: String? = null,
    @SerializedName(value = "bankName", alternate = ["bank_name"])
    val bankName: String? = null,
    @SerializedName(value = "safeBank", alternate = ["isSafeBank"])
    val isSafeBank: Boolean = false
)

data class ExternalTransferRequest(
    val amount: Double,
    val bankCode: String,
    val accountNumber: String,
    val narration: String
)

data class ExternalTransferResponse(
    val reference: String? = null,
    val status: String? = null,
    val message: String? = null
)

data class ExternalTransferReceipt(
    val amount: Double,
    val recipientName: String,
    val recipientAccountNumber: String,
    val recipientBank: String,
    val narration: String,
    val reference: String,
    val dateTime: String,
    val status: String
)
