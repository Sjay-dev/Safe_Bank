package com.example.safebank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.safebank.Navigation.AppNavHost

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
