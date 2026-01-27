package com.example.safebank.View.DashBoard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.LocalTime

@Composable
fun GreetingBar(name: String) {

    var greetings by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val timeNow = LocalTime.now().hour
            greetings = when (timeNow) {
                in 0..11 -> "Good Morning"
                in 12..16 -> "Good Afternoon"
                in 17..23 -> "Good Evening"
                else -> "Hello"
            }
            delay(60 * 1000)
        }
    }

    Text("")
}