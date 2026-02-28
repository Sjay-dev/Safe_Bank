package com.example.safebank.Model.Safe_Bank_Api

import com.example.safebank.Model.Entities.AuthRequest
import com.example.safebank.Model.Entities.AuthResponse
import com.example.safebank.Model.Entities.TransferRequest
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Entities.UserRequest
import retrofit2.Response
import retrofit2.http.*

interface SafeBankApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: UserRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/transfers")
    suspend fun performTransfer(
        @Body request: TransferRequest,
        @Header("Authorization") token: String
    ): TransferResponse

    @GET("api/ping")
    suspend fun ping(): Response<String>
}

    //      Transfer History

//    @GET("api/transfers/history")
//    suspend fun getTransferHistory(
//        @Header("Authorization") token: String,
//        @Query("page") page: Int = 0,
//        @Query("size") size: Int = 10
//    ): TransferHistoryResponse
//}