package com.example.safebank.Navigation

sealed class Screen(val route : String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object DashBoard : Screen("dashboard")
    object TransferScreen : Screen("transfer_screen")
}



