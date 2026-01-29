package com.example.safebank.View.DashBoard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashBoard(){
    Surface(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
        ,
        color = MaterialTheme.colorScheme.background
    ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(15.dp)
                ,
                        horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GreetingBar("Sj")

                Spacer(Modifier.padding(10.dp))

                BalanceCard("$500" , "Bonus + Cash")

                Spacer(Modifier.padding(15.dp))

                QuickActionsRow()

                Spacer(Modifier.padding(15.dp))

                RecentTransactionsSection()
            }
    }
}