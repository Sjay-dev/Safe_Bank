package com.example.safebank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.safebank.Navigation.AppNavHost
import com.example.safebank.View.Auth.LoginScreen
import com.example.safebank.View.Auth.SignUpScreen
import com.example.safebank.View.DashBoard.DashBoard
import com.example.safebank.View.DashBoard.ScaffoldScreen
import com.example.safebank.ui.theme.SafeBankTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            SafeBankTheme {
                AppNavHost(navController)
            }
            }
        }
    }
