package com.example.safebank.View.DashBoard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safebank.Model.Entities.Bank
import com.example.safebank.Model.Entities.ExternalTransferReceipt
import com.example.safebank.View.Components.SuggestionChip
import com.example.safebank.ViewModel.ExternalTransferViewModel

@Composable
fun ExternalTransferScreen(
    onBackClick: () -> Unit,
    onTransferSuccess: (ExternalTransferReceipt) -> Unit,
    viewModel: ExternalTransferViewModel = hiltViewModel()
) {
    var accountNumber by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var narration by rememberSaveable { mutableStateOf("") }
    var showBankPicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.loadBanks() }
    LaunchedEffect(viewModel.receipt) {
        viewModel.receipt?.let { receipt ->
            onTransferSuccess(receipt)
            viewModel.consumeReceipt()
        }
    }
    LaunchedEffect(viewModel.error) {
        viewModel.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 88.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Transfer to Other Banks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Secure transfers to Nigerian banks",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            TransferFormCard {
                Text("Recipient bank", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                BankSelectionField(
                    bankName = viewModel.selectedBank?.name.orEmpty(),
                    isLoading = viewModel.isLoadingBanks,
                    onClick = { showBankPicker = true }
                )

                Spacer(Modifier.height(16.dp))
                Text("Account number", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { value ->
                        accountNumber = value.filter(Char::isDigit).take(10)
                        viewModel.onAccountNumberChanged(accountNumber)
                    },
                    label = { Text("10-digit account number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                RecipientStatus(viewModel)
            }

            Spacer(Modifier.height(16.dp))

            TransferFormCard {
                Text("Transfer details", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₦)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = narration,
                    onValueChange = { narration = it },
                    label = { Text("Narration (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Transfer purpose",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SuggestionChip("Purchase") { narration = "Purchase" }
                    SuggestionChip("Personal Transfer") { narration = "Personal Transfer" }
                }
            }
        }

        val amountValue = amount.toDoubleOrNull()
        Button(
            onClick = { viewModel.submit(accountNumber, amountValue!!, narration) },
            enabled = accountNumber.length == 10 &&
                amountValue != null &&
                amountValue > 0 &&
                viewModel.selectedBank != null &&
                viewModel.recipientName != null &&
                !viewModel.isSafeBankAccount &&
                !viewModel.isSubmitting,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp)
        ) {
            if (viewModel.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Send money")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
        )
    }

    if (showBankPicker) {
        BankPickerDialog(
            banks = viewModel.banks,
            onBankSelected = { bank ->
                showBankPicker = false
                viewModel.selectBank(bank, accountNumber)
            },
            onDismiss = { showBankPicker = false }
        )
    }
}

@Composable
private fun BankSelectionField(
    bankName: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Select bank",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = bankName.ifEmpty { "Choose recipient bank" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (bankName.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select bank",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TransferFormCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun RecipientStatus(viewModel: ExternalTransferViewModel) {
    Spacer(Modifier.height(10.dp))
    when {
        viewModel.isResolving -> Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
            Spacer(Modifier.width(8.dp))
            Text("Resolving account…", style = MaterialTheme.typography.bodySmall)
        }

        viewModel.isSafeBankAccount -> Text(
            text = "SafeBank account: ${viewModel.recipientName}. Use Transfer to SafeBank for this recipient.",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )

        viewModel.recipientName != null -> Text(
            text = viewModel.recipientName!!,
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Bold
        )

        viewModel.selectedBank == null -> Text(
            text = "Select a bank to resolve the account name.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun BankPickerDialog(
    banks: List<Bank>,
    onBankSelected: (Bank) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val matches = remember(query, banks) {
        banks.filter { bank ->
            bank.name.contains(query, ignoreCase = true) || bank.code.contains(query, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select bank") },
        text = {
            Column {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search banks") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(Modifier.heightIn(max = 360.dp)) {
                    items(matches, key = { it.code }) { bank ->
                        Text(
                            text = bank.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onBankSelected(bank) }
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
