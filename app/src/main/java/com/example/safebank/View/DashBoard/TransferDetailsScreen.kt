package com.example.safebank.View.DashBoard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.safebank.View.Components.SuggestionChip
import com.example.safebank.Navigation.TransactionReceiptRoute
import com.example.safebank.ViewModel.TransferViewModel
import com.example.safebank.ViewModel.UserViewModel


@Composable
fun TransferDetailsScreen(
    name: String,
    senderAccountNumber: String,
    accountNumber: String,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val viewModel: TransferViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()


    var amount by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.transferResult) {

        viewModel.transferResult?.let { result ->

         userViewModel.refreshBalance(senderAccountNumber)

            navController.navigate(
                TransactionReceiptRoute(
                    amount = result.amount.toString(),
                    recipientName = result.receiverName,
                    recipientBank = "SafeBank",
                    recipientAccount = result.receiverAccountNumber,
                    narration = result.description,
                    reference = result.transferId.toString(),
                    dateTime = result.createdAt,
                    status = result.status
                )
            )
            viewModel.consumeTransferResult()
        }
    }

    Box(modifier = Modifier.fillMaxSize()
        .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // 🔝 Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, null)
                }

                Text(
                    "Transfer",
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold
                )
            }

            // 👤 Recipient
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Person, null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(name, fontWeight = FontWeight.Bold)
                        Text(accountNumber, color = Color.Gray)
                    }
                }
            }

            // 💰 Amount Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Amount")

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text("₦10.00 - 5,000,000.00") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))


                }
            }

            Spacer(Modifier.height(16.dp))

            // 📝 Remark
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Remark")

                    OutlinedTextField(
                        value = remark,
                        onValueChange = { remark = it },
                        placeholder = { Text("What's this for? (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Row {
                        SuggestionChip("Purchase") { remark = "Purchase" }
                        Spacer(Modifier.width(8.dp))
                        SuggestionChip("Personal Transfer") { remark = "Personal Transfer" }
                    }
                }
            }
        }

        // 🚀 Confirm Button
        Button(
            onClick = {
                val amountValue = amount.toDoubleOrNull()
                if (amountValue != null) {
                    viewModel.performTransfer(
                        accountNumber = accountNumber,
                        amount = amountValue,
                        remark = remark
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            enabled = amount.isNotBlank() && !viewModel.isLoading
        ) {
            Text(if (viewModel.isLoading) "Processing..." else "Confirm")
        }
    }
}





