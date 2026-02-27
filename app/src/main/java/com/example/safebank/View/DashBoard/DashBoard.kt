package com.example.safebank.View.DashBoard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.safebank.Navigation.BottomScreen
import com.example.safebank.View.Settings.SettingsScreen


@Composable
fun ScaffoldScreen(navController :  NavHostController){

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomScreen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomScreen.Home.route) {
                DashBoard()
            }

            composable(BottomScreen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun DashBoard(){

    Surface(
        modifier = Modifier.fillMaxSize(),
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

@Composable
fun BottomNavBar(navController: NavController){
    val items = listOf(
        BottomScreen.Home,
        BottomScreen.Settings
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(BottomScreen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(screen.icon, contentDescription = screen.label)
                },
                label = {
                    Text(screen.label)
                }
            )
        }
    }
}