package com.example.safebank.View.DashBoard


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safebank.Navigation.TransferRoute
import com.example.safebank.Navigation.ExternalTransferRoute
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun QuickActionsRow(navController: NavController) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth() ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionItem(
            icon = Icons.Default.Send,
            label = "Transfer to SafeBank" ,
            onClick = {
                navController.navigate(TransferRoute)
            }
        )

        QuickActionItem(
            icon = Icons.Default.Receipt,
            label = "Transfer to Other Banks",
            onClick = { navController.navigate(ExternalTransferRoute) }
        )

        QuickActionItem(
            icon = Icons.Default.PhoneAndroid,
            label = "Pay Bills",
            onClick = { Toast.makeText(context, "Feature coming soon.", Toast.LENGTH_SHORT).show() }
        )
    }
}


@Composable
fun QuickActionItem(
    icon: ImageVector,
    label: String ,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


