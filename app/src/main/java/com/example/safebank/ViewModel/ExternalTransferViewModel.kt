package com.example.safebank.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebank.Model.Entities.Bank
import com.example.safebank.Model.Entities.ExternalTransferReceipt
import com.example.safebank.Model.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ExternalTransferViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    var banks by mutableStateOf<List<Bank>>(emptyList()); private set
    var selectedBank by mutableStateOf<Bank?>(null); private set
    var recipientName by mutableStateOf<String?>(null); private set
    var isSafeBankAccount by mutableStateOf(false); private set
    var isLoadingBanks by mutableStateOf(false); private set
    var isResolving by mutableStateOf(false); private set
    var isSubmitting by mutableStateOf(false); private set
    var error by mutableStateOf<String?>(null); private set
    var receipt by mutableStateOf<ExternalTransferReceipt?>(null); private set

    private var resolutionJob: Job? = null

    fun loadBanks() = viewModelScope.launch {
        if (banks.isNotEmpty() || isLoadingBanks) return@launch
        isLoadingBanks = true
        error = null
        runCatching { repository.getBanks() }
            .onSuccess { banks = it.sortedBy(Bank::name) }
            .onFailure { error = it.message ?: "Unable to load banks" }
        isLoadingBanks = false
    }

    fun selectBank(bank: Bank, accountNumber: String) {
        selectedBank = bank
        resolve(accountNumber)
    }

    fun onAccountNumberChanged(accountNumber: String) {
        recipientName = null
        isSafeBankAccount = false
        error = null
        resolutionJob?.cancel()
        if (accountNumber.length == ACCOUNT_NUMBER_LENGTH) resolve(accountNumber)
    }

    private fun resolve(accountNumber: String) {
        if (accountNumber.length != ACCOUNT_NUMBER_LENGTH) return
        resolutionJob?.cancel()
        resolutionJob = viewModelScope.launch {
            // A SafeBank lookup is intentionally first: this makes detection independently
            // upgradeable without coupling the UI to bank-code heuristics.
            delay(350)
            val safeBankUser = runCatching { repository.getUserByAccountNumber(accountNumber) }.getOrNull()
            if (safeBankUser != null) {
                isSafeBankAccount = true
                recipientName = safeBankUser.name
                return@launch
            }
            val bank = selectedBank ?: return@launch
            isResolving = true
            runCatching { repository.resolveBankAccount(accountNumber, bank.code) }
                .onSuccess {
                    recipientName = it.accountName
                    isSafeBankAccount = it.isSafeBank
                    if (it.isSafeBank && it.bankCode != null) {
                        banks.firstOrNull { bank -> bank.code == it.bankCode }?.let { detected -> selectedBank = detected }
                    }
                }
                .onFailure { error = it.message ?: "Unable to resolve this account" }
            isResolving = false
        }
    }

    fun submit(accountNumber: String, amount: Double, narration: String) = viewModelScope.launch {
        val bank = selectedBank ?: return@launch
        if (recipientName == null || isSafeBankAccount) return@launch
        isSubmitting = true
        error = null
        receipt = null
        runCatching { repository.performExternalTransfer(accountNumber, bank.code, amount, narration) }
            .onSuccess { response ->
                receipt = ExternalTransferReceipt(
                    amount = amount,
                    recipientName = recipientName.orEmpty(),
                    recipientAccountNumber = accountNumber,
                    recipientBank = bank.name,
                    narration = narration,
                    reference = response.reference.orEmpty(),
                    dateTime = Instant.now().toString(),
                    status = response.status ?: "Successful"
                )
            }
            .onFailure { error = it.message ?: "Transfer failed" }
        isSubmitting = false
    }

    fun consumeReceipt() {
        receipt = null
    }

    companion object { private const val ACCOUNT_NUMBER_LENGTH = 10 }
}
