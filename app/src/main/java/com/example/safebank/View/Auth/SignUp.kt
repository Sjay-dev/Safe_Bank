package com.example.safebank.View.Auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignUpScreen(){
    Surface(modifier = Modifier.fillMaxSize()
        .statusBarsPadding()
        , color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Welcome Back!")

            OutlinedTextField()
        }
    }
}

@Composable
@Preview
fun previewSignUpScreen(){
    SignUpScreen()
}