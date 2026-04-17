package com.example.safebank.View.DashBoard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.safebank.Model.Entities.TransferResponse
import com.example.safebank.Model.Repository.UserRepository

@Composable
fun RecentTransactionsSection(
    transactions: List<TransferResponse>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Transactions",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "See all",
                    color = Color(0xFF2563EB)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            transactions.take(5).forEach { transaction ->

                val isCredit =
                    transaction.transactionType == "CREDIT"

                val title =
                    if (isCredit)
                        transaction.senderName
                    else
                        transaction.receiverName

                TransactionItem(
                    iconColor = if (isCredit)
                        Color(0xFF16A34A)
                    else Color.Red,

                    title = title,

                    subtitle = transaction.description,

                    amount =
                        if (isCredit)
                            "+₦${transaction.amount}"
                        else
                            "-₦${transaction.amount}"
                )
            }
        }
    }
}


@Composable
fun TransactionItem(
    iconColor: Color,
    title: String,
    subtitle: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconColor.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(
                subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = amount,
            fontWeight = FontWeight.SemiBold
        )
    }
}
