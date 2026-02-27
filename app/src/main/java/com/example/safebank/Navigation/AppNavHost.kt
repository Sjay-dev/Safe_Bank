package com.example.safebank.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safebank.View.Auth.LoginScreen
import com.example.safebank.View.Auth.SignUpScreen
import com.example.safebank.View.DashBoard.ScaffoldScreen


@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }
        composable(Screen.DashBoard.route)   { ScaffoldScreen(navController) }
        }
    }
