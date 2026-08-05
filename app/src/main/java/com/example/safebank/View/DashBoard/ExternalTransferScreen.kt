package com.example.safebank.View.DashBoard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safebank.Model.Entities.Bank
import com.example.safebank.ViewModel.ExternalTransferViewModel

@Composable
fun ExternalTransferScreen(
    onBackClick: () -> Unit,
    viewModel: ExternalTransferViewModel = hiltViewModel()
) {
    var accountNumber by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var narration by rememberSaveable { mutableStateOf("") }
    var showBankPicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { viewModel.loadBanks() }
    LaunchedEffect(viewModel.successMessage) {
        viewModel.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onBackClick()
        }
    }
    LaunchedEffect(viewModel.error) { viewModel.error?.let { snackbarHostState.showSnackbar(it) } }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).statusBarsPadding()) {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(bottom = 84.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") }
                    Text("Transfer to Other Banks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Text("Secure transfers to Nigerian banks", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.height(16.dp))
                TransferFormCard {
                    Text("Recipient bank", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.selectedBank?.name ?: "",
                        onValueChange = {}, readOnly = true,
                        label = { Text("Select bank") },
                        trailingIcon = { if (viewModel.isLoadingBanks) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) },
                        modifier = Modifier.fillMaxWidth().clickable { showBankPicker = true }
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
                        label = { Text("10-digit account number") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()
                    )
                    RecipientStatus(viewModel)
                }
                Spacer(Modifier.height(16.dp))
                TransferFormCard {
                    OutlinedTextField(amount, { amount = it }, Modifier.fillMaxWidth(), label = { Text("Amount (₦)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(narration, { narration = it }, Modifier.fillMaxWidth(), label = { Text("Narration (optional)") })
                }
            }
            val amountValue = amount.toDoubleOrNull()
            Button(
                onClick = { viewModel.submit(accountNumber, amountValue!!, narration) },
                enabled = accountNumber.length == 10 && amountValue != null && amountValue > 0 &&
                    viewModel.selectedBank != null && viewModel.recipientName != null && !viewModel.isSafeBankAccount && !viewModel.isSubmitting,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp)
            ) { Text(if (viewModel.isSubmitting) "Sending…" else "Send money") }
        }
    }
    if (showBankPicker) BankPickerDialog(viewModel.banks, { bank ->
        showBankPicker = false; viewModel.selectBank(bank, accountNumber)
    }, { showBankPicker = false })
}

@Composable private fun TransferFormCard(content: @Composable ColumnScope.() -> Unit) =
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Column(Modifier.padding(16.dp), content = content) }

@Composable private fun RecipientStatus(viewModel: ExternalTransferViewModel) {
    Spacer(Modifier.height(10.dp))
    when {
        viewModel.isResolving -> Row(verticalAlignment = Alignment.CenterVertically) { CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp); Spacer(Modifier.width(8.dp)); Text("Resolving account…") }
        viewModel.isSafeBankAccount -> Text("SafeBank account: ${viewModel.recipientName}. Use Transfer to SafeBank for this recipient.", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
        viewModel.recipientName != null -> Text(viewModel.recipientName!!, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
        viewModel.selectedBank == null -> Text("Select a bank to resolve the account name.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable private fun BankPickerDialog(banks: List<Bank>, onBankSelected: (Bank) -> Unit, onDismiss: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val matches = remember(query, banks) { banks.filter { it.name.contains(query, true) || it.code.contains(query, true) } }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Select bank") }, text = {
        Column {
            OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text("Search banks") }, trailingIcon = { Icon(Icons.Default.Search, null) })
            LazyColumn(Modifier.heightIn(max = 360.dp)) { items(matches, key = { it.code }) { bank -> Text(bank.name, Modifier.fillMaxWidth().clickable { onBankSelected(bank) }.padding(vertical = 16.dp)) } }
        }
    }, confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}
