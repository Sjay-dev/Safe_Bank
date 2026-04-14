package com.example.safebank.Model.Safe_Bank_Api

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getToken(): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("token", null) ?: ""
    }

    fun saveToken(token: String) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("token", token).apply()

    }
}