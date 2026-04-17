package com.example.safebank.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.safebank.View.Auth.LoginScreen
import com.example.safebank.View.Auth.SignUpScreen
import com.example.safebank.View.DashBoard.ScaffoldScreen
import com.example.safebank.View.DashBoard.TransactionReceiptScreen
import com.example.safebank.View.DashBoard.TransferDetailsScreen
import com.example.safebank.View.DashBoard.TransferScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LoginRoute) {


        composable<LoginRoute> {
            LoginScreen(navController)
        }

        composable<SignUpRoute> {
            SignUpScreen(navController)
        }

        composable<TransferRoute> {
            TransferScreen(
                onBackClick = { navController.popBackStack() },
                onTransferClick = { accountNumber, recipientName ->
                    navController.navigate(
                        TransferDetailsRoute(
                            accountNumber = accountNumber,
                            recipientName = recipientName
                        )
                    )
                }
            )
        }

        composable<TransferDetailsRoute> { backStackEntry ->
            val route: TransferDetailsRoute = backStackEntry.toRoute()

            TransferDetailsScreen(
                accountNumber = route.accountNumber,
                name = route.recipientName,
                navController = navController,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            "receipt/{amount}/{receiverName}/{receiverAccount}/{senderName}/{senderAccount}/{date}"
        ) {

            TransactionReceiptScreen(
                navController = navController,
                amount = it.arguments?.getString("amount") ?: "",
                recipientName = it.arguments?.getString("receiverName") ?: "",
                recipientAccount = it.arguments?.getString("receiverAccount") ?: "",
                dateTime = it.arguments?.getString("date") ?: ""
            )
        }

        composable<MainRoute> { backStackEntry ->
            val route: MainRoute = backStackEntry.toRoute()

            ScaffoldScreen(
                navController = navController,
                name = route.name,
                accountNumber = route.accountNumber,
                balance = route.balance,
                token = route.token
            )
        }
    }
}
