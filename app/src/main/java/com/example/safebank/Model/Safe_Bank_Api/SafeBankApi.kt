package com.example.safebank.Model.Safe_Bank_Api

import com.example.safebank.Model.Entities.AuthRequest
import com.example.safebank.Model.Entities.AuthResponse
import com.example.safebank.Model.Entities.GoogleAuthRequest
import com.example.safebank.Model.Entities.TransferHistoryResponse
import com.example.safebank.Model.Entities.TransferRequest
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Entities.UserRequest
import com.example.safebank.Model.Entities.UserResponse
import com.example.safebank.Model.Entities.Bank
import com.example.safebank.Model.Entities.AccountResolution
import com.example.safebank.Model.Entities.ExternalTransferRequest
import com.example.safebank.Model.Entities.ExternalTransferResponse
import retrofit2.Response
import retrofit2.http.*

interface SafeBankApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: UserRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): AuthResponse

    @POST("api/transfers")
    suspend fun performTransfer(
        @Body request: TransferRequest
    ): TransferResponse

    @GET("api/ping")
    suspend fun ping(): Response<String>

    @GET("api/users/{accountNumber}")
    suspend fun getUserByAccountNumber(
        @Path("accountNumber") accountNumber: String,
    ): Response<UserResponse>

    @GET("api/transfers/history")
    suspend fun getTransferHistory(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): TransferHistoryResponse

    @GET("api/banks")
    suspend fun getBanks(): Response<List<Bank>>

    @GET("api/banks/resolve")
    suspend fun resolveBankAccount(
        @Query("account_number") accountNumber: String,
        @Query("bank_code") bankCode: String
    ): Response<AccountResolution>

    @POST("api/transfers/external")
    suspend fun performExternalTransfer(
        @Body request: ExternalTransferRequest
    ): Response<ExternalTransferResponse>

    }


