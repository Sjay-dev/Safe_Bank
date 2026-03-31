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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransferScreen(
    onBackClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onTransferClick: (String) -> Unit
) {
    var accountInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔝 Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Transfer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onHistoryClick) {
                Text("History", color = primaryColor)
            }
        }

        // 🟦 Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor.copy(alpha = 0.1f))
        ) {
            Text(
                text = "Instant • Secure • Free",
                modifier = Modifier.padding(16.dp),
                color = primaryColor,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 💳 Recipient Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Recipient Account",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountInput,
                    onValueChange = { accountInput = it },
                    placeholder = {
                        Text("Enter account number")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Don’t know the account? Ask them",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📑 Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = primaryColor
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Recents") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Favourites") }
            )
        }

        // 👥 List
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(getDummyUsers()) { user ->
                UserItem(user = user)
            }
        }

        // 🚀 Transfer Button
        Button(
            onClick = { onTransferClick(accountInput) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = accountInput.isNotBlank(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun UserItem(user: UserUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.name.first().toString(),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(user.name, fontWeight = FontWeight.Medium)
            Text(
                user.accountNumber,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

data class UserUi(
    val name: String,
    val accountNumber: String
)

fun getDummyUsers() = listOf(
    UserUi("John Doe", "1234567890"),
    UserUi("Jane Smith", "0987654321"),
    UserUi("Michael Lee", "1122334455")
)