package com.example.safebank.View.DashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safebank.Navigation.MainRoute

@Composable
fun TransactionReceiptScreen(
    navController: NavController,
    amount: String,
    recipientName: String,
    recipientBank: String = "SafeBank",
    recipientAccount: String,
    dateTime: String,
    status: String = "Successful"
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SafeBank", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8))
                        Text("Transaction Receipt", fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(24.dp))
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF10B981), modifier = Modifier.size(42.dp).align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(8.dp))
                    Text(amount, fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981), modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(status, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(dateTime, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    DetailRow("Recipient Details", recipientName, "$recipientBank | $recipientAccount")
                    Spacer(Modifier.height(16.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    navController.popBackStack<MainRoute>(false)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, title: String, subtitle: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, modifier = Modifier.weight(1f))
        Column(modifier = Modifier.weight(1.2f), horizontalAlignment = Alignment.End) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color.Gray, fontSize = 13.sp)
        }
    }
}