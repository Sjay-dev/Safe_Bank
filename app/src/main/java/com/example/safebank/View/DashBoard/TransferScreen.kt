package com.example.safebank.View.DashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safebank.ViewModel.TransferViewModel

@Composable
fun TransferScreen(
    onBackClick: () -> Unit,
    onTransferClick: (String) -> Unit
) {
    var accountNumber by remember { mutableStateOf("") }

    // ✅ Hilt ViewModel
    val viewModel: TransferViewModel = androidx.hilt.navigation.compose.hiltViewModel()

    // ✅ Direct state (since you're using mutableStateOf in ViewModel)
    val recipientName = viewModel.recipientName
    val error = viewModel.error
    val isLoading = viewModel.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 80.dp)
        ) {

            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = "Transfer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = "Instant • Secure • Free",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Recipient Account", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = {
                            accountNumber = it

                            // ✅ Trigger API at 10 digits
                            if (it.length == 10) {
                                viewModel.fetchUser(it)
                            } else {
                                // Reset state if user edits again
                                viewModel.recipientName = null
                                viewModel.error = null
                            }
                        },
                        placeholder = { Text("Enter account number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ Status UI
                    when {
                        isLoading -> {
                            Text("Checking account...", color = Color.Gray)
                        }

                        recipientName != null -> {
                            Text(
                                text = recipientName,
                                color = Color(0xFF2E7D32), // nicer green
                                fontWeight = FontWeight.Bold
                            )
                        }

                        error != null -> {
                            Text(
                                text = error,
                                color = Color.Red
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Don’t know the account number? Ask them",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Bottom Button
        Button(
            onClick = { onTransferClick(accountNumber) },
            enabled = accountNumber.length == 10 && recipientName != null, // ✅ safer
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Continue")
        }
    }
}

