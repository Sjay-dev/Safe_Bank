package com.example.safebank.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.safebank.View.Auth.LoginScreen
import com.example.safebank.View.Auth.SignUpScreen
import com.example.safebank.View.DashBoard.ScaffoldScreen
import com.example.safebank.View.DashBoard.TransactionReceiptScreen
import com.example.safebank.View.DashBoard.TransferDetailsScreen
import com.example.safebank.View.DashBoard.TransferScreen
import com.example.safebank.View.DashBoard.ExternalTransferScreen
import com.example.safebank.ViewModel.TransferViewModel
import com.example.safebank.ViewModel.UserViewModel


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LoginRoute) {


        composable<LoginRoute> {
            LoginScreen(navController)
        }

        composable<SignUpRoute> {
            SignUpScreen(navController)
        }

        composable<TransferRoute> { backStackEntry ->
            val mainRoute = remember(backStackEntry) {
                navController.getBackStackEntry<MainRoute>().toRoute<MainRoute>()
            }
            TransferScreen(
                onBackClick = { navController.popBackStack() },
                onTransferClick = { accountNumber, recipientName ->
                    navController.navigate(
                        TransferDetailsRoute(
                            accountNumber = accountNumber,
                            recipientName = recipientName,
                            senderAccountNumber = mainRoute.accountNumber
                        )
                    )
                }
            )
        }

        composable<ExternalTransferRoute> { backStackEntry ->
            val mainBackStackEntry = remember(backStackEntry) {
                navController.getBackStackEntry<MainRoute>()
            }
            val mainRoute = mainBackStackEntry.toRoute<MainRoute>()
            val userViewModel: UserViewModel = hiltViewModel(mainBackStackEntry)
            val transferViewModel: TransferViewModel = hiltViewModel(mainBackStackEntry)

            ExternalTransferScreen(
                onBackClick = { navController.popBackStack() },
                onTransferSuccess = { receipt ->
                    userViewModel.refreshBalance(mainRoute.accountNumber)
                    transferViewModel.loadTransactions(mainRoute.token)
                    navController.navigate(
                        TransactionReceiptRoute(
                            amount = receipt.amount.toString(),
                            recipientName = receipt.recipientName,
                            recipientBank = receipt.recipientBank,
                            recipientAccount = receipt.recipientAccountNumber,
                            narration = receipt.narration,
                            reference = receipt.reference,
                            dateTime = receipt.dateTime,
                            status = receipt.status
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
                senderAccountNumber = route.senderAccountNumber
            )
        }

        composable<TransactionReceiptRoute> { backStackEntry ->
            val route: TransactionReceiptRoute = backStackEntry.toRoute()
            TransactionReceiptScreen(
                navController = navController,
                amount = route.amount.toDoubleOrNull() ?: 0.0,
                recipientName = route.recipientName,
                recipientBank = route.recipientBank,
                recipientAccount = route.recipientAccount,
                narration = route.narration,
                reference = route.reference,
                dateTime = route.dateTime,
                status = route.status
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
