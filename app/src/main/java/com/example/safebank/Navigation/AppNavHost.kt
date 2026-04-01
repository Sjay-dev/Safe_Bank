package com.example.safebank.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.safebank.View.Auth.LoginScreen
import com.example.safebank.View.Auth.SignUpScreen
import com.example.safebank.View.DashBoard.ScaffoldScreen
import com.example.safebank.View.DashBoard.TransferScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) { LoginScreen(navController) }

        composable(Screen.SignUp.route) { SignUpScreen(navController) }

        composable(Screen.TransferScreen.route) {
            TransferScreen(
                onBackClick = { navController.popBackStack() },
                onTransferClick = { accountNumber ->
                    navController.navigate("transferDetails/$accountNumber")
                }
            )
        }
        composable(
            route = "main/{name}/{accountNumber}/{balance}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("accountNumber") { type = NavType.StringType },
                navArgument("balance") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val accountNumber = backStackEntry.arguments?.getString("accountNumber") ?: ""
            val balance = backStackEntry.arguments?.getString("balance") ?: ""
            ScaffoldScreen(navController, name, accountNumber, balance)
        }
    }
}
